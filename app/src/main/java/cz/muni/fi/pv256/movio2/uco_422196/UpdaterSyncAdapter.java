package cz.muni.fi.pv256.movio2.uco_422196;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ja on 10.1.2018.
 */

public class UpdaterSyncAdapter extends AbstractThreadedSyncAdapter {

    public static final int SYNC_INTERVAL = 86400;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;
    public static final String SYNC_FINISHED = "sync_finished";

    private static final String TAG = UpdaterSyncAdapter.class.getSimpleName();

    public UpdaterSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SyncRequest request = new SyncRequest.Builder()
                    .syncPeriodic(syncInterval, flexTime)
                    .setSyncAdapter(account, authority)
                    .setExtras(Bundle.EMPTY)
                    .build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account, authority, Bundle.EMPTY, syncInterval);
        }
    }

    public static void syncImmediately(Context context) {
        if (BuildConfig.logging) Log.d(TAG, "Sync immediately");
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context), context.getString(R.string.content_authority), bundle);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }

    public static Account getSyncAccount(Context context) {
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        Account newAccount = new Account(context.getString(R.string.app_name), context.getString(R.string.sync_account_type));
        if (null == accountManager.getPassword(newAccount)) {
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        UpdaterSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);
        syncImmediately(context);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        if (BuildConfig.logging)
            Log.d(TAG, "Starting synchronization");
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(App.API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            FilmAPI service = retrofit.create(FilmAPI.class);
            FilmDbHelper dbHelper = new FilmDbHelper(getContext());
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            FilmManager filmManager = new FilmManager(database);

            List<Film> favouriteFilms = filmManager.getFavouriteFilms();
            for (int i = 0; i < favouriteFilms.size(); i++) {
                Film film = favouriteFilms.get(i);
                Call<FilmDTO> request = service.getFilmById(film.getId());
                FilmDTO m = request.execute().body();
                Film updatedFilm = new Film(Long.parseLong(m.getId(), 10), m.getReleaseDateAsLong(), m.getCoverPath(), m.getTitle(), m.getSmallPath(), m.getPopularityAsFloat(), m.getDescription());

                if (!compareFilms(film, updatedFilm)) {
                    if (BuildConfig.logging)
                        Log.d(TAG, updatedFilm.getTitle() + " updated");
                    filmManager.removeFilm(film);
                    filmManager.createFilm(updatedFilm);
                    Notification.Builder n = new Notification.Builder(getContext());
                    Intent intent = new Intent(getContext(), PV256aktivita.class);
                    PendingIntent pIntent = PendingIntent.getActivity(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    n.setContentTitle("PV256project").setAutoCancel(true)
                            .setContentText(updatedFilm.getTitle() + " updated.")
                            .setContentIntent(pIntent);

                    NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(0, n.build());
                }
            }
            if (BuildConfig.logging)
                Log.d(TAG, "Synchronization finished.");
            Intent i = new Intent(SYNC_FINISHED);
            getContext().sendBroadcast(i);
            database.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            if (BuildConfig.logging) Log.d(TAG, "Synchronization exception.");
        }
    }

    private boolean compareFilms(Film older, Film newer){
        return  (Math.abs(older.getReleaseDate() - newer.getReleaseDate()) <= 0.000001 &&
                older.getCoverPath().equals(newer.getCoverPath()) &&
                older.getTitle().equals(newer.getTitle()) &&
                older.getSmallPath().equals(newer.getSmallPath()) &&
                Math.abs(older.getPopularity() - newer.getPopularity()) <= 0.000001 &&
                older.getDescription().equals(newer.getDescription())
        );
    }
}

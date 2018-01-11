package cz.muni.fi.pv256.movio2.uco_422196;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import static cz.muni.fi.pv256.movio2.uco_422196.UpdaterSyncAdapter.SYNC_FINISHED;

public class PV256aktivita extends AppCompatActivity implements FilmListFragment.OnFilmSelectListener {

    private boolean mTwoPane;
    private boolean mSwitched;
    private SwitchCompat mSwitchButton;
    private Button mRefreshButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pv256aktivita);
        if (findViewById(R.id.film_detail_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.film_detail_container, new FilmDetailFragment(), FilmDetailFragment.TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
        if (savedInstanceState != null && savedInstanceState.containsKey("switch")) {
            mSwitched = savedInstanceState.getBoolean("switch");
        }
        UpdaterSyncAdapter.initializeSyncAdapter(this);
    }

    private static IntentFilter syncIntentFilter = new IntentFilter(SYNC_FINISHED);

    private BroadcastReceiver syncFinishedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Data updated", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onPause() {
        unregisterReceiver(syncFinishedReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(syncFinishedReceiver, syncIntentFilter);
    }


    @Override
    public void onFilmSelect(Film film) {
        if (mTwoPane) {
            FragmentManager fm = getSupportFragmentManager();
            FilmDetailFragment fragment = FilmDetailFragment.newInstance(film);
            fm.beginTransaction()
                    .replace(R.id.film_detail_container, fragment, FilmDetailFragment.TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, FilmDetailActivity.class);
            intent.putExtra(FilmDetailActivity.EXTRAFILM, film);
            intent.putExtra(FilmDetailActivity.SWITCH, mSwitchButton.isChecked());
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem itemSwitch = menu.findItem(R.id.menuSwitch);
        itemSwitch.setActionView(R.layout.menu_switch);
        mSwitchButton = (SwitchCompat) itemSwitch.getActionView().findViewById(R.id.switchForActionBar);
        mSwitchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    compoundButton.setText("Favourites");
                    compoundButton.setChecked(true);
                    FilmListFragment filmListFragment = (FilmListFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_list);
                    filmListFragment.setIsFavourite(true);
                    filmListFragment.updateData();
                } else {
                    compoundButton.setText("Discover");
                    compoundButton.setChecked(false);
                    FilmListFragment filmListFragment = (FilmListFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_list);
                    filmListFragment.setIsFavourite(false);
                    filmListFragment.updateData();
                }
            }
        });
        mSwitchButton.setChecked(mSwitched);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuRefresh){
            UpdaterSyncAdapter.syncImmediately(getApplicationContext());
            Toast.makeText(getApplicationContext(), "Updating film data", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mSwitched = getIntent().getBooleanExtra("switch", false);
        ((FilmListFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_list)).updateData();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
            outState.putBoolean("switch", mSwitchButton.isChecked());
            super.onSaveInstanceState(outState);
        }
}
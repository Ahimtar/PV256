package cz.muni.fi.pv256.movio2.uco_422196;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;


/**
 * Created by Ja on 5.10.2017.
 */

public class App extends Application {

    public static final String API_KEY = "2ecbe6e664fc80573e3c18f711b34555";
    public static final String API_URL = "http://api.themoviedb.org/";

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            initStrictMode();
        }
    }

    private void initStrictMode() {
        StrictMode.ThreadPolicy.Builder tpb = new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            tpb.penaltyFlashScreen();
        }
        StrictMode.setThreadPolicy(tpb.build());

        StrictMode.VmPolicy.Builder vmpb = new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .penaltyLog();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            vmpb.detectLeakedClosableObjects();
        }
        StrictMode.setVmPolicy(vmpb.build());
    }
}

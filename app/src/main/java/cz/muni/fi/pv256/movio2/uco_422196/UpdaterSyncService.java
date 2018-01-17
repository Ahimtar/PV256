package cz.muni.fi.pv256.movio2.uco_422196;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Ja on 10.1.2018.
 */

public class UpdaterSyncService extends Service {

    private static final Object LOCK = new Object();
    private static UpdaterSyncAdapter sUpdaterSyncAdapter = null;

    @Override
    public void onCreate() {
        synchronized (LOCK) {
            if (sUpdaterSyncAdapter == null) {
                sUpdaterSyncAdapter = new UpdaterSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sUpdaterSyncAdapter.getSyncAdapterBinder();
    }
}

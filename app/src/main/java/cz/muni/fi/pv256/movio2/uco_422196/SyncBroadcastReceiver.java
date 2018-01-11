package cz.muni.fi.pv256.movio2.uco_422196;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Ja on 10.1.2018.
 */

public class SyncBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        UpdaterSyncAdapter.getSyncAccount(context);
    }
}

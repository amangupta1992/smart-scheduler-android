package io.hypertrack.smart_scheduler;

import android.content.Context;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

/**
 * Created by piyush on 25/11/16.
 */
public class Utils {
    private static final String TAG = Utils.class.getSimpleName();

    public static boolean checkIfPowerSaverModeEnabled(Context context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                final PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
                return pm.isPowerSaveMode();
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception occurred while checkIfPowerSaverModeEnabled: " + e);
        }

        return false;
    }
}

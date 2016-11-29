package io.hypertrack.smart_scheduler;

import android.util.Log;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;

/**
 * Created by piyush on 25/11/16.
 */
public class SmartSchedulerPeriodicTaskService extends GcmTaskService {
    private static final String TAG = SmartSchedulerPeriodicTaskService.class.getSimpleName();

    @Override
    public int onRunTask(TaskParams taskParams) {
        Log.i(TAG, "SmartSchedulerPeriodicTaskService PeriodicTask.onRunTask called");

        SmartScheduler smartScheduler = SmartScheduler.getInstance(getApplicationContext());
        smartScheduler.onPeriodicTaskJobScheduled(taskParams.getTag(), taskParams.getExtras());
        return GcmNetworkManager.RESULT_SUCCESS;
    }
}
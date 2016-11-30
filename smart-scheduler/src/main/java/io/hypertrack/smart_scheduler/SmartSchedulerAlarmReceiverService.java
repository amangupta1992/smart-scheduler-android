package io.hypertrack.smart_scheduler;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by piyush on 25/11/16.
 */
public class SmartSchedulerAlarmReceiverService extends IntentService {

    private static final String TAG = SmartSchedulerAlarmReceiverService.class.getSimpleName();

    public SmartSchedulerAlarmReceiverService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null && intent.getExtras() != null) {
            Bundle bundle = intent.getExtras();
            final Integer jobID = bundle.getInt(SmartScheduler.ALARM_JOB_ID_KEY, -1);

            SmartScheduler jobScheduler = SmartScheduler.getInstance(getApplicationContext());
            if (jobScheduler != null) {
                jobScheduler.onAlarmJobScheduled(jobID);
                return;
            }

            Log.e(TAG, "Error occurred while SmartSchedulerAlarmReceiverService: jobScheduler is NULL");
        }
    }
}

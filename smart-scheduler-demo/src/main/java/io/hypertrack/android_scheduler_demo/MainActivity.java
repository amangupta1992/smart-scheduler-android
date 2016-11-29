package io.hypertrack.android_scheduler_demo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import io.hypertrack.smart_scheduler.Job;
import io.hypertrack.smart_scheduler.SmartScheduler;

public class MainActivity extends AppCompatActivity implements SmartScheduler.JobScheduledCallback {

    private static final int JOB_ID = 1;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String JOB_PERIODIC_TASK_TAG = "io.hypertrack.android_scheduler_demo.JobPeriodicTask";

    private Spinner jobTypeSpinner, networkTypeSpinner;
    private Switch requiresChargingSwitch, isPeriodicSwitch;
    private EditText intervalInMillisEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI Views
        jobTypeSpinner = (Spinner) findViewById(R.id.spinnerJobType);
        networkTypeSpinner = (Spinner) findViewById(R.id.spinnerNetworkType);
        requiresChargingSwitch = (Switch) findViewById(R.id.switchRequiresCharging);
        isPeriodicSwitch = (Switch) findViewById(R.id.switchPeriodicJob);
        intervalInMillisEditText = (EditText) findViewById(R.id.jobInterval);
    }

    public void onAddJobBtnClick(View view) {
        Job job = createJob();
        if (job == null) {
            Toast.makeText(MainActivity.this, "Invalid paramteres specified. " +
                    "Please try again with correct job params.", Toast.LENGTH_SHORT).show();
            return;
        }

        SmartScheduler jobScheduler = SmartScheduler.getInstance(this);
        if (jobScheduler.addJob(job)) {
            Toast.makeText(MainActivity.this, "Job successfully added!", Toast.LENGTH_SHORT).show();
        }
    }

    private Job createJob() {
        int jobType = getJobType();
        int networkType = getNetworkTypeForJob();
        boolean requiresCharging = requiresChargingSwitch.isChecked();
        boolean isPeriodic = isPeriodicSwitch.isChecked();

        String intervalInMillisString = intervalInMillisEditText.getText().toString();
        if (TextUtils.isEmpty(intervalInMillisString)) {
            return null;
        }

        Long intervalInMillis = Long.parseLong(intervalInMillisString);
        Job.Builder builder = new Job.Builder(JOB_ID, this, jobType, JOB_PERIODIC_TASK_TAG)
                .setRequiredNetworkType(networkType)
                .setRequiresCharging(requiresCharging)
                .setIntervalMillis(intervalInMillis);

        if (isPeriodic) {
            builder.setPeriodic(intervalInMillis);
        }

        return builder.build();
    }

    private int getJobType() {
        int jobTypeSelectedPos = jobTypeSpinner.getSelectedItemPosition();
        int jobType = Job.Type.JOB_TYPE_NONE;
        switch (jobTypeSelectedPos) {
            case 1:
                jobType = Job.Type.JOB_TYPE_HANDLER;
                break;
            case 2:
                jobType = Job.Type.JOB_TYPE_ALARM;
                break;
            case 3:
                jobType = Job.Type.JOB_TYPE_PERIODIC_TASK;
                break;
        }

        return jobType;
    }

    private int getNetworkTypeForJob() {
        int networkTypeSelectedPos = networkTypeSpinner.getSelectedItemPosition();
        int networkType = Job.NetworkType.NETWORK_TYPE_ANY;
        switch (networkTypeSelectedPos) {
            case 1:
                networkType = Job.NetworkType.NETWORK_TYPE_CONNECTED;
                break;
            case 2:
                networkType = Job.NetworkType.NETWORK_TYPE_UNMETERED;
                break;
        }

        return networkType;
    }

    public void onRemoveJobBtnClick(View view) {
        SmartScheduler jobScheduler = SmartScheduler.getInstance(this);
        if (!jobScheduler.contains(JOB_ID)) {
            Toast.makeText(MainActivity.this, "No job exists with JobID: " + JOB_ID, Toast.LENGTH_SHORT).show();
            return;
        }

        if (jobScheduler.removeJob(JOB_ID)) {
            Toast.makeText(MainActivity.this, "Job successfully removed!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onJobScheduled(Context context, final Job job) {
        if (job != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "Job: " + job.getJobId() + " scheduled!", Toast.LENGTH_SHORT).show();
                }
            });
            Log.d(TAG, "Job: " + job.getJobId() + " scheduled!");
        }
    }
}

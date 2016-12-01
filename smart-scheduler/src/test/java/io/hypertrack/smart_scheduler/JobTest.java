package io.hypertrack.smart_scheduler;

import android.content.Context;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by piyush on 01/12/16.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JobTest {

    private static final String JOB_PERIODIC_TASK_TAG = "JobTestPeriodicTaskTag";
    private static final int JOB_ID = 1;

    private SmartScheduler.JobScheduledCallback callback;

    @Test
    public void testGenerateJobID() throws Exception {
        int jobID = Job.generateJobID();
        assertThat(jobID > 0, is(true));
    }

    @Test
    public void testGetJobId() throws Exception {
        Job job = createJob(false, Job.NetworkType.NETWORK_TYPE_ANY, false, 1000);
        assertThat(job.getJobId(), is(JOB_ID));
    }

    @Test
    public void testGetJobType() throws Exception {
        Job job = createJob(false, Job.NetworkType.NETWORK_TYPE_ANY, false, 1000);
        assertThat(job.getJobType(), is(Job.Type.JOB_TYPE_HANDLER));
    }

    @Test
    public void testGetJobScheduledCallback() throws Exception {
        Job job = createJob(false, Job.NetworkType.NETWORK_TYPE_ANY, false, 1000);
        assertThat(job.getJobScheduledCallback(), is(callback));
    }

    @Test
    public void testGetPeriodicTaskTag() throws Exception {
        Job job = createJob(false, Job.NetworkType.NETWORK_TYPE_ANY, false, 1000);
        assertThat(job.getPeriodicTaskTag(), is(JOB_PERIODIC_TASK_TAG));
    }

    @Test
    public void testGetRequiresCharging() throws Exception {
        Job job = createJob(true, Job.NetworkType.NETWORK_TYPE_ANY, false, 1000);
        assertThat(job.getRequiresCharging(), is(true));
    }

    @Test
    public void testGetNetworkType() throws Exception {
        Job job = createJob(false, Job.NetworkType.NETWORK_TYPE_CONNECTED, false, 1000);
        assertThat(job.getNetworkType(), is(Job.NetworkType.NETWORK_TYPE_CONNECTED));
    }

    @Test
    public void testIsPeriodic() throws Exception {
        Job job = createJob(false, Job.NetworkType.NETWORK_TYPE_ANY, true, 1000);
        assertThat(job.isPeriodic(), is(true));
    }

    @Test
    public void testGetIntervalMillis() throws Exception {
        Job job = createJob(false, Job.NetworkType.NETWORK_TYPE_ANY, true, 1000);
        assertThat(job.getIntervalMillis(), is(1000L));
    }

    private Job createJob(boolean requiresCharging, int networkType, boolean isPeriodic,
                          int intervalInMillis) {
        callback = new SmartScheduler.JobScheduledCallback() {
            @Override
            public void onJobScheduled(Context context, Job job) {
                // do nothing
            }
        };

        Job.Builder builder = new Job.Builder(JOB_ID, callback, JOB_PERIODIC_TASK_TAG)
                .setRequiresCharging(requiresCharging)
                .setRequiredNetworkType(networkType)
                .setIntervalMillis(intervalInMillis);

        if (isPeriodic) {
            builder.setPeriodic(intervalInMillis);
        }

        return builder.build();
    }
}
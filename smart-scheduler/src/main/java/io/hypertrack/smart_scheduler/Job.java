package io.hypertrack.smart_scheduler;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by piyush on 07/10/16.
 */
public class Job {

    /** Network Types*/
    public abstract class NetworkType {
        /** Default */
        public static final int NETWORK_TYPE_ANY = 2;
        /** This job requires network connectivity */
        public static final int NETWORK_TYPE_CONNECTED = 0;
        /** This job requires network connectivity that is unmetered */
        public static final int NETWORK_TYPE_UNMETERED = 1;
    }

    /** Job Types*/
    public abstract class Type {
        /** Default */
        public static final int JOB_TYPE_NONE = 0;
        /** Use Handler type job if the frequency required for the Job is small enough that it can't
         * be accomplished by using PeriodicTasks */
        public static final int JOB_TYPE_HANDLER = 1;
        /** Use Periodic_Task type job if the frequency required for the Job
         * can be accomplished by using PeriodicTasks */
        public static final int JOB_TYPE_PERIODIC_TASK = 2;
        /** Use Alarm type job if the frequency required for the Job is large enough to be using alarms */
        public static final int JOB_TYPE_ALARM = 3;
    }
    // Threshold to schedule via Handlers
    protected static final long JOB_TYPE_HANDLER_THRESHOLD = 60000;

    /** Job parameters */
    private final int jobId;
    private final int jobType;
    private final SmartScheduler.JobScheduledCallback jobScheduledCallback;
    private final String periodicTaskTag;
    private final boolean requireCharging;
    private final int networkType;
    private final boolean isPeriodic;
    private final long intervalMillis;

    /**
     * Method to generate unique JobId
     * @return
     */
    public static int generateJobID() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    /**
     * Unique job id associated with this class. This is assigned to your job by the scheduler.
     */
    public int getJobId() {
        return jobId;
    }

    /**
     * One of {@link Job.Type#JOB_TYPE_HANDLER},
     * {@link Job.Type#JOB_TYPE_PERIODIC_TASK}, or
     * {@link Job.Type#JOB_TYPE_ALARM}.
     */
    public int getJobType() {
        return jobType;
    }

    /**
     * Name of the callback class that will be called when Job is scheduled by the SmartScheduler.
     */
    public SmartScheduler.JobScheduledCallback getJobScheduledCallback() {
        return jobScheduledCallback;
    }

    /**
     * Tag for the PeriodicTask to be set for {@link Job.Type#JOB_TYPE_PERIODIC_TASK} job.
     */
    public String getPeriodicTaskTag() {
        return periodicTaskTag;
    }

    /**
     * Whether this job needs the device to be plugged in.
     */
    public boolean getRequiresCharging() {
        return requireCharging;
    }

    /**
     * One of {@link Job.NetworkType#NETWORK_TYPE_CONNECTED},
     * {@link Job.NetworkType#NETWORK_TYPE_ANY}, or
     * {@link Job.NetworkType#NETWORK_TYPE_UNMETERED}.
     */
    public int getNetworkType() {
        return networkType;
    }

    /**
     * Track whether this job will repeat with a given period.
     */
    public boolean isPeriodic() {
        return isPeriodic;
    }

    /**
     * Set to the interval between occurrences of this job. This value is <b>not</b> set if the
     * job does not recur periodically.
     */
    public long getIntervalMillis() {
        return intervalMillis;
    }

    private Job(Job.Builder b) {
        jobId = b.mJobId;
        jobType = b.mJobType;
        jobScheduledCallback = b.mJobScheduledCallback;
        periodicTaskTag = b.mPeriodicTaskTag;
        requireCharging = b.mRequiresCharging;
        networkType = b.mNetworkType;
        isPeriodic = b.mIsPeriodic;
        intervalMillis = b.mIntervalMillis;
    }

    /**
     * Builder class for constructing {@link Job} objects.
     */
    public static final class Builder {
        private int mJobId;
        private int mJobType = Job.Type.JOB_TYPE_NONE;
        private SmartScheduler.JobScheduledCallback mJobScheduledCallback;

        // PeriodicTask Type Job Parameters
        private String mPeriodicTaskTag;

        // Requirements.
        private boolean mRequiresCharging = false;
        private int mNetworkType = Job.NetworkType.NETWORK_TYPE_ANY;

        // Time interval parameter.
        private long mIntervalMillis = 60000;

        // Periodic parameters.
        private boolean mIsPeriodic = false;

        /**
         * @param jobScheduledCallback The endpoint that you implement that will receive the callback from the
         *                             SmartScheduler.
         * @param periodicTaskTag      Tag for the PeriodicTask to be set for PeriodicTask type jobs.
         */
        public Builder(SmartScheduler.JobScheduledCallback jobScheduledCallback,
                       @NonNull String periodicTaskTag) {
            generateJobID();
            mJobScheduledCallback = jobScheduledCallback;
            mPeriodicTaskTag = periodicTaskTag;
        }

        /**
         * @param jobScheduledCallback The endpoint that you implement that will receive the callback from the
         *                             SmartScheduler.
         * @param jobType              Type of Job to be scheduled
         * @param periodicTaskTag      Tag for the PeriodicTask to be set for PeriodicTask type jobs.
         */
        public Builder(SmartScheduler.JobScheduledCallback jobScheduledCallback, int jobType,
                       @Nullable String periodicTaskTag) {
            generateJobID();
            mJobScheduledCallback = jobScheduledCallback;
            mPeriodicTaskTag = periodicTaskTag;
            mJobType = jobType;
        }

        /**
         * @param jobId                Application-provided id for this job. Subsequent calls to cancel, or
         *                             jobs created with the same jobId, will update the pre-existing job with
         *                             the same id.
         * @param jobScheduledCallback The endpoint that you implement that will receive the callback from the
         *                             SmartScheduler.
         * @param periodicTaskTag      Tag for the PeriodicTask to be set for PeriodicTask type jobs.
         */
        public Builder(int jobId, SmartScheduler.JobScheduledCallback jobScheduledCallback,
                       @NonNull String periodicTaskTag) {
            mJobScheduledCallback = jobScheduledCallback;
            mPeriodicTaskTag = periodicTaskTag;
            mJobId = jobId;
        }

        /**
         * @param jobId                Application-provided id for this job. Subsequent calls to cancel, or
         *                             jobs created with the same jobId, will update the pre-existing job with
         *                             the same id.
         * @param jobScheduledCallback The endpoint that you implement that will receive the callback from the
         *                             SmartScheduler.
         * @param jobType              Type of Job to be scheduled
         * @param periodicTaskTag      Tag for the PeriodicTask to be set for PeriodicTask type jobs.
         */
        public Builder(int jobId, SmartScheduler.JobScheduledCallback jobScheduledCallback, int jobType,
                       @Nullable String periodicTaskTag) {
            mJobScheduledCallback = jobScheduledCallback;
            mPeriodicTaskTag = periodicTaskTag;
            mJobType = jobType;
            mJobId = jobId;
        }

        /**
         * Specify that to run {@link Job.Type#JOB_TYPE_PERIODIC_TASK} job. This is the tag for the
         * PeriodicTask to be set for {@link Job.Type#JOB_TYPE_PERIODIC_TASK} job.
         *
         * @param periodicTaskTag tag for the PeriodicTask to be set for
         *                        {@link Job.Type#JOB_TYPE_PERIODIC_TASK} job.
         */
        public Builder setPeriodicTaskTag(String periodicTaskTag) {
            mPeriodicTaskTag = periodicTaskTag;
            return this;
        }

        /**
         * Set some description of the kind of network type your job needs to have.
         * Not calling this function means the network is not necessary, as the default is
         * {@link Job.NetworkType#NETWORK_TYPE_ANY}.
         * Bear in mind that calling this function defines network as a strict requirement for your
         * job. If the network requested is not available your job will never run.
         */
        public Builder setRequiredNetworkType(int networkType) {
            mNetworkType = networkType;
            return this;
        }

        /**
         * Specify that to run this job, the device needs to be plugged in. This defaults to
         * false.
         *
         * @param requiresCharging Whether or not the device is plugged in.
         */
        public Builder setRequiresCharging(boolean requiresCharging) {
            mRequiresCharging = requiresCharging;
            return this;
        }

        /**
         * Specify that this job should happen only once after the provided interval has elapsed.
         *
         * @param intervalMillis Millisecond interval after which this job has to be performed.
         */
        public Builder setIntervalMillis(long intervalMillis) {
            mIsPeriodic = false;
            mIntervalMillis = intervalMillis;
            return this;
        }

        /**
         * Specify that this job should recur with the provided interval, not more than once per
         * period.
         *
         * @param intervalMillis Millisecond interval for which this job will repeat.
         */
        public Builder setPeriodic(long intervalMillis) {
            mIsPeriodic = true;
            mIntervalMillis = intervalMillis;
            return this;
        }

        /**
         * @return The job object to hand to the SmartScheduler. This object is immutable.
         */
        public Job build() {
            if (mJobType == Job.Type.JOB_TYPE_NONE) {

                // Schedule via Handlers if mIntervalMillis is less than JOB_TYPE_HANDLER_THRESHOLD
                if (mIntervalMillis < JOB_TYPE_HANDLER_THRESHOLD) {
                    mJobType = Job.Type.JOB_TYPE_HANDLER;

                    // Schedule via PeriodicTask if job requires charging or network connectivity
                } else if (mRequiresCharging || mNetworkType != Job.NetworkType.NETWORK_TYPE_ANY) {
                    mJobType = Job.Type.JOB_TYPE_PERIODIC_TASK;

                } else {
                    mJobType = Job.Type.JOB_TYPE_ALARM;
                }
            }

            return new Job(this);
        }
    }

    @Override
    public String toString() {
        return "Job{" +
                "jobId=" + jobId +
                ", jobType=" + jobType +
                ", jobScheduledCallback=" + (jobScheduledCallback != null ? jobScheduledCallback : " null") +
                ", requireCharging=" + requireCharging +
                ", networkType=" + networkType +
                ", isPeriodic=" + isPeriodic +
                ", intervalMillis=" + intervalMillis +
                '}';
    }
}

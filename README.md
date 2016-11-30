# smart-scheduler-android

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/672a8b4b2bfc4f7d86c07e22a435515a)](https://www.codacy.com/app/piyushguptaece/smart-scheduler-android?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=hypertrack/smart-scheduler-android&amp;utm_campaign=Badge_Grade)

## Overview

A utility library for Android to schedule one-time or periodic jobs while your app is running. Currently, Android OS supports 3 types of scheduling APIs: `Handler`, `AlarmManager` and `JobScheduler`. The choice of one suitable API, the inflexibility of switching between them and the amount of boilerplate code required for setting up makes it difficult to use these APIs. 

Want to know more on this and wondering why you should prefer using this library over doing it yourself. Check out the [blog post](https://blog.hypertrack.io/?p=6713).

## Usage

* The class `SmartScheduler` serves as the entry point. You need to create a `Job` object with the corresponding job parameters using the `Job.Builder` class.

* The `Job.Builder` class has many extra options, e.g. you can specify a required network connection, required charging state, make the job periodic or run the job at an exact time.

```
	SmartScheduler.JobScheduledCallback callback = new SmartScheduler.JobScheduledCallback() {
        @Override
        public void onJobScheduled(Context context, Job job) {
            // Handle onJobScheduled here
        }
    };

    Job.Builder builder = new Job.Builder(JOB_ID, callback, jobType, JOB_PERIODIC_TASK_TAG)
            .setRequiredNetworkType(networkType)
            .setRequiresCharging(requiresCharging)
            .setIntervalMillis(intervalInMillis);

    if (isPeriodic) {
        builder.setPeriodic(intervalInMillis);
    }

    Job job = builder.build();
```

* Each job has a unique ID. This ID helps to identify the job later to update requirements or to cancel the job. In case this unique ID is not specified in the `Job` object, one will be auto-generated using `Job.generateJobID()` method.

* Once a `Job` object has been created with the relevant parameters, you can add this job using `SmartScheduler` class.

```
	SmartScheduler jobScheduler = SmartScheduler.getInstance(getApplicationContext());
    boolean result = jobScheduler.addJob(job);

    if (result) {
        // Job successfully added here
    }
```

* A `Non-Periodic` Job will be removed automatically once it has been scheduled successfully. For `Periodic` Jobs, call `SmartScheduler.removeJob(jobID)` method to remove the job.

```
	SmartScheduler jobScheduler = SmartScheduler.getInstance(getApplicationContext());
    boolean result = jobScheduler.removeJob(JOB_ID);

	if (result) {
        // Job successfully removed here
    }
```

## Contribute
Please use the [issues tracker](https://github.com/hypertrack/smart-scheduler-android/issues) to raise bug reports and feature requests. We'd love to see your pull requests, so send them in!

## About HyperTrack
Developers use HyperTrack to build location features, not infrastructure. We reduce the complexity of building and operating location features to a few APIs that just work.
Check it out. [Sign up](https://dashboard.hypertrack.io/signup/) and start building! Join our [Slack community](http://slack.hypertrack.io) for instant responses. You can also email us at help@hypertrack.io

# smart-scheduler
[![Slack Status](http://slack.hypertrack.io/badge.svg)](http://slack.hypertrack.io)

## Overview

An utility library for Android to schedule one time or periodic jobs while your app is running. Currently, Android OS supports 3 types of scheduling APIs: `Handler`, `AlarmManager` and `JobScheduler`.
Choosing which API to use depends on the certain conditions. 

#### `AlarmManager` API: 

##### PROs:
* The `AlarmManager` API is intended for cases where you want to have your application code run at a specific time, even if your application itself is not currently running. 
* It is available on all devices and all OS versions.
* It is easier to send Broadcast to start a service delayed using this API.

##### CONS:
* Periodic `Alarm` only work in case the interval between them is >= `1 sec`.


#### `Handler` API:

##### PROs:
* For normal timing operations (ticks, timeouts, etc) it is easier and much more efficient to use `Handler`.
* `Handler` API is preferred where scheduling needs to happen with an interval < `1 sec`.

##### CONS:
* `Handler` API works better only for periodic jobs which need to be performed while the app is in the foreground.


#### `JobScheduler` API:

##### PROs:
* The `JobScheduler` API is relatively easier to use and provides a lot of controls to the developer to schedule jobs better.
* This API helps is respecting device state and prevents exploitation of device resources by apps.

##### CONS:
* This is only available on Android Lollipop and above (21*).
* `JobScheduler` API with Network dependency deos not get scheduled if the period is below 30 seconds.
* It starts failing if the power saver mode has been enabled on the device.


#### `GcmNetworkManager` API:

##### PROs:
* The `GcmNetworkManager` API is similar to `JobScheduler` API.
* This API can be used on Android Gingerbread and above (9*).

##### CONS:
* It is only available on devices with Google Play preinstalled.
* `PeriodicTask` or `GcmNetworkManager` APIs will not be scheduled if the period is below 30 seconds. 
* `PeriodicTask` API starts failing if the power saver mode has been enabled on the device.


## Usage

* The class `SmartScheduler` serves as entry point. You need to create a `Job` object with the corresponding job parameters using the `Job.Builder` class.

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

* Each job has a unique ID. This ID helps to identify the job later to update requirements or to cancel the job. In case this unique ID is not specified in the `Job` object, one will be auto*generated using `Job.generateJobID()` method.

* Once an `Job` object has been created with the relevant parameters, you can add this job using `SmartScheduler` class.

```
	SmartScheduler jobScheduler = SmartScheduler.getInstance(getApplicationContext());
    boolean result = jobScheduler.addJob(job);

    if (result) {
        // Job successfully added here
    }
```

* A `Non*Periodic` Job will be removed automatically once it has been scheduled successfully. For `Periodic` Jobs, call `SmartScheduler.removeJob(jobID)` method to remove job.

```
	SmartScheduler jobScheduler = SmartScheduler.getInstance(getApplicationContext());
    boolean result = jobScheduler.removeJob(JOB_ID);

	if (result) {
        // Job successfully removed here
    }
```

## Contribute
Please use the [issues tracker](https://github.com/hypertrack/smart-scheduler-android/issues) to raise bug reports and feature requests. We'd love to see your pull requests * send them in!

## Support
Join our [Slack community](http://slack.hypertrack.io) for instant responses. You can also email us at help@hypertrack.io

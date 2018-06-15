@file:JvmName("ServiceFactory")

package com.example.astrodashalib.service

import android.annotation.TargetApi
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.astrodashalib.Constant
import com.example.astrodashalib.helper.DateTimeUtil
import com.example.astrodashalib.service.device.DeviceIdJobService
import com.example.astrodashalib.service.device.DeviceIdService
import com.example.astrodashalib.service.faye.FayeJobService
import java.util.*

/**
 * Created by himanshu on 03/10/17.
 */


fun scheduleFayeIntentService(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        try {
            val mJobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            val builder = JobInfo.Builder(Constant.FAYE_SERVICE_JOB_ID, ComponentName(context.packageName, FayeJobService::class.java.name))
            builder.setPersisted(true)
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
            mJobScheduler.schedule(builder.build())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}


fun scheduleDeviceIdService(context: Context) {
    val bool = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) scheduleDeviceIdServiceForApiLessThan21(context) else scheduleDeviceIdServiceForAPI21(context)
}

private fun scheduleDeviceIdServiceForApiLessThan21(context: Context): Boolean {
    try {
        val cal = Calendar.getInstance()
        cal.apply {
            set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
        val pintent = PendingIntent.getService(context, Constant.DEVICE_ID_REQUEST_CODE, Intent(context, DeviceIdService::class.java), PendingIntent.FLAG_UPDATE_CURRENT)
        val alarm = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.timeInMillis, AlarmManager.INTERVAL_DAY * 7, pintent)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return true
}

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
private fun scheduleDeviceIdServiceForAPI21(context: Context): Boolean {
    try {
        val mJobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val builder = JobInfo.Builder(Constant.DEVICE_ID_SERVICE_JOB_ID, ComponentName(context.packageName, DeviceIdJobService::class.java.name))
        builder.setPersisted(true)
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
        val daysLeft = DateTimeUtil.daysUntilSunday(Date())
        val minLatencyMillis = daysLeft * DateTimeUtil.INTERVAL_1DAY
        builder.setMinimumLatency(minLatencyMillis)
        mJobScheduler.schedule(builder.build())
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return true
}
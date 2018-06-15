package com.example.astrodashalib.service.faye

import android.app.job.JobParameters
import android.app.job.JobService
import android.os.Build
import android.support.annotation.RequiresApi
import com.example.astrodashalib.isNetworkAvailable
import com.example.astrodashalib.view.modules.chat.ChatDetailActivity

/**
 * Created by himanshu on 09/10/17.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
class FayeJobService : JobService() {


    override fun onStartJob(params: JobParameters?): Boolean {
        try {
            when (applicationContext.isNetworkAvailable()) {
                true -> {
                    if ((ChatDetailActivity.inBackground == null || ChatDetailActivity.inBackground))
                        FayeIntentService.startFayeService(applicationContext)
                }
                else -> {

                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    override fun onStopJob(params: JobParameters?): Boolean = true
}
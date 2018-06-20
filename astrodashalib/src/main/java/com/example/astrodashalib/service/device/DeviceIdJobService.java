package com.example.astrodashalib.service.device;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.astrodashalib.R;

/**
 * Created by himanshu on 03/10/17.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class DeviceIdJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        try {
            DeviceIdService.startService(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }
}

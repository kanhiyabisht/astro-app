package com.example.astrodashalib.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Surface
import android.view.TextureView
import com.example.astrodashalib.R
import com.example.astrodashalib.helper.getPhoneNumber
import com.example.astrodashalib.helper.isPhoneVerificationShown
import com.example.astrodashalib.view.modules.phoneVerification.PhoneVerificationActivity
import kotlinx.android.synthetic.main.activity_splash_screen.*
import java.io.IOException
import java.lang.ref.WeakReference


class SplashScreen : AppCompatActivity(), TextureView.SurfaceTextureListener {

    var mMediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_splash_screen)
            splashTextureView.setSurfaceTextureListener(this)

        } catch (e: Exception) {
            e.printStackTrace()
            startNextActivity()
        }

    }

    fun startNextActivity() {
        try {
            if (isFinishing)
                return
            if (!isPhoneVerificationShown() && getPhoneNumber().isEmpty())
                PhoneVerificationActivity.createIntent(this).let {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(it)
                }
            else ChatDetailActivity.createIntent(this).let {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(it)
            }
            finish()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onResume() {
        try {
            super.onResume()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {

    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {

    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean = false

    override fun onSurfaceTextureAvailable(surfaceTexture: SurfaceTexture?, width: Int, height: Int) {
        val surface = Surface(surfaceTexture)

        try {
            val afd = assets.openFd(FILE_NAME)
            mMediaPlayer = MediaPlayer()
            mMediaPlayer?.apply {
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                setSurface(surface)
                setLooping(false)
                prepareAsync()

                // Play video when the media source is ready for playback.
                setOnPreparedListener(MediaPlayer.OnPreparedListener { mediaPlayer ->
                    try {
                        mediaPlayer.start()
                        initiateHandler()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                })
            }


        } catch (e: IllegalArgumentException) {
            Log.d(TAG, e.message)
        } catch (e: SecurityException) {
            Log.d(TAG, e.message)
        } catch (e: IllegalStateException) {
            Log.d(TAG, e.message)
        } catch (e: IOException) {
            Log.d(TAG, e.message)
        }
    }

    fun initiateHandler() {
        SPLASH_HANDLER.postDelayed(MyRunnable(this), DELAY_MILLIS.toLong())
    }


    class SplashHandler : Handler()

    private class MyRunnable(activity: Activity) : Runnable {
        val context: WeakReference<Context>
        val outerActivity: WeakReference<Activity>

        init {
            context = WeakReference(activity.applicationContext)
            outerActivity = WeakReference(activity)
        }

        override fun run() {
            try {
                val ctx = context.get()
                if (!isPhoneVerificationShown() && getPhoneNumber().isEmpty())
                    PhoneVerificationActivity.createIntent(ctx).let {
                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        ctx?.startActivity(it)
                    }
                else ChatDetailActivity.createIntent(ctx).let {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    ctx?.startActivity(it)
                }

                outerActivity.get()?.finish()

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    override fun onDestroy() {
        try {
            super.onDestroy()
            mMediaPlayer?.let {
                try {
                    it.stop()
                } catch (e: IllegalStateException) {
                    e.printStackTrace()
                }

                it.release()
            }
            mMediaPlayer = null

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    companion object {
        val SPLASH_HANDLER = SplashHandler()
        val DELAY_MILLIS: Int = 3000
        val TAG = "SplashScreen"

        private val FILE_NAME = "splash_screen_video.mp4"
    }
}

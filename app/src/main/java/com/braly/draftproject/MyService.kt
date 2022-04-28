package com.braly.draftproject

import android.app.Service
import android.content.Intent
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.CancellationSignal
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat

class MyService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private val TAG = "MyService"
    private fun showToast(msg: String){
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate() {
        super.onCreate()
        initializeAppLockerNotification()
    }
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Handler().postDelayed({
            val cancellationSignal = CancellationSignal()
            val biometricCallback = @RequiresApi(Build.VERSION_CODES.P)
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                    super.onAuthenticationSucceeded(result)
                    showToast("onAuthenticationSucceeded")
                    Log.e(TAG, "onAuthenticationSucceeded: ")
                    stopService(Intent(this@MyService, MyService::class.java))
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                    Log.e(TAG, "onAuthenticationError: $errorCode  :  $errString")
                    showToast("onAuthenticationError: $errorCode  :  $errString")
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Log.e(TAG, "onAuthenticationFailed: ")
                    showToast("onAuthenticationFailed: ")
                }

                override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) {
                    super.onAuthenticationHelp(helpCode, helpString)
                    Log.e(TAG, "onAuthenticationHelp: $helpCode  :  $helpString")
                    showToast("onAuthenticationHelp: $helpCode  :  $helpString")
                }
            }
            BiometricPrompt.Builder(applicationContext)
                .setTitle("Touch ID to open")
                .setNegativeButton("ok", applicationContext.mainExecutor) { _, _ ->
                    Log.e(
                        TAG,
                        "authenticateFingerPrint: negative button",

                        )
                }
                .build()
                .authenticate(
                    cancellationSignal,
                    applicationContext.mainExecutor,
                    biometricCallback
                )
        }, 3000)

        return START_STICKY
    }
    private fun initializeAppLockerNotification() {
        val notification = ServiceNotificationManager(applicationContext).createNotification()
        NotificationManagerCompat.from(applicationContext)
            .notify(123, notification)
        startForeground(123, notification)
    }
}
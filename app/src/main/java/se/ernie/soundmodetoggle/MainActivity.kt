package se.ernie.soundmodetoggle

import android.app.Activity
import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager

            // Check if first run or permission not granted
            val prefs = getSharedPreferences("se.ernie.soundmodetoggle.prefs", Context.MODE_PRIVATE)
            val isFirstRun = prefs.getBoolean("first_run", true)

            if (isFirstRun || !notificationManager.isNotificationPolicyAccessGranted) {
                prefs.edit().putBoolean("first_run", false).apply()
                val intent = android.content.Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                startActivity(intent)
                finish()
                return
            }
        }

        toggleSoundMode()
        vibrateBriefly()
        finish()
    }

    private fun toggleSoundMode() {
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        try {
            val currentMode = audioManager.ringerMode
            if (currentMode == AudioManager.RINGER_MODE_NORMAL) {
                audioManager.ringerMode = AudioManager.RINGER_MODE_VIBRATE
            } else {
                audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
            }
        } catch (e: Exception) {
            vibrateBriefly()
        }
    }

    private fun vibrateBriefly() {
        // Get vibrator service based on Android version
        val vibrator = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(100)
        }
    }
}
package se.ernie.soundmodetoggle

import android.app.Activity
import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.widget.Toast

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager

            // Check if first run or permission not granted
            val prefs = ModeSettings.prefs(this)
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
            // Cycle Sound -> Vibrate -> Silent -> Sound, skipping modes turned off in settings
            val enabled = ModeSettings.enabledModes(ModeSettings.prefs(this))
            audioManager.ringerMode = ModeSettings.nextMode(audioManager.ringerMode, enabled).value

            // Read the mode back, some devices adjust the requested mode (e.g. Silent under DND)
            showMode(audioManager.ringerMode)
        } catch (e: Exception) {
            vibrateBriefly()
        }
    }

    private fun showMode(mode: Int) {
        val ringerMode = RingerMode.of(mode) ?: return
        Toast.makeText(applicationContext, ringerMode.label, Toast.LENGTH_SHORT).show()
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
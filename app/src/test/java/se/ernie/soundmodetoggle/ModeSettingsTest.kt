package se.ernie.soundmodetoggle

import android.media.AudioManager
import org.junit.Assert.assertEquals
import org.junit.Test

class ModeSettingsTest {
    private val all = RingerMode.entries

    @Test
    fun cyclesThroughAllModes() {
        assertEquals(RingerMode.VIBRATE, ModeSettings.nextMode(AudioManager.RINGER_MODE_NORMAL, all))
        assertEquals(RingerMode.SILENT, ModeSettings.nextMode(AudioManager.RINGER_MODE_VIBRATE, all))
        assertEquals(RingerMode.SOUND, ModeSettings.nextMode(AudioManager.RINGER_MODE_SILENT, all))
    }

    @Test
    fun skipsDisabledMode() {
        val soundAndSilent = listOf(RingerMode.SOUND, RingerMode.SILENT)
        assertEquals(RingerMode.SILENT, ModeSettings.nextMode(AudioManager.RINGER_MODE_NORMAL, soundAndSilent))
        assertEquals(RingerMode.SOUND, ModeSettings.nextMode(AudioManager.RINGER_MODE_SILENT, soundAndSilent))
    }

    @Test
    fun leavesModeThatIsNotEnabled() {
        // e.g. switched to Silent with the volume keys while only Sound and Vibrate are enabled
        val soundAndVibrate = listOf(RingerMode.SOUND, RingerMode.VIBRATE)
        assertEquals(RingerMode.SOUND, ModeSettings.nextMode(AudioManager.RINGER_MODE_SILENT, soundAndVibrate))

        val vibrateAndSilent = listOf(RingerMode.VIBRATE, RingerMode.SILENT)
        assertEquals(RingerMode.VIBRATE, ModeSettings.nextMode(AudioManager.RINGER_MODE_NORMAL, vibrateAndSilent))
    }

    @Test
    fun unknownModeStartsFromSound() {
        assertEquals(RingerMode.SOUND, ModeSettings.nextMode(-1, all))
        assertEquals(RingerMode.VIBRATE, ModeSettings.nextMode(-1, listOf(RingerMode.VIBRATE, RingerMode.SILENT)))
    }
}

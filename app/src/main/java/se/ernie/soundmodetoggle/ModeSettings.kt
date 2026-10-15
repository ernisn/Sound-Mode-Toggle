package se.ernie.soundmodetoggle

import android.content.Context
import android.content.SharedPreferences
import android.media.AudioManager

// Ringer modes in the order the app cycles through them
enum class RingerMode(val value: Int, val prefKey: String, val label: Int) {
    SOUND(AudioManager.RINGER_MODE_NORMAL, "mode_sound_enabled", R.string.mode_sound),
    VIBRATE(AudioManager.RINGER_MODE_VIBRATE, "mode_vibrate_enabled", R.string.mode_vibrate),
    SILENT(AudioManager.RINGER_MODE_SILENT, "mode_silent_enabled", R.string.mode_silent);

    companion object {
        fun of(value: Int): RingerMode? = entries.firstOrNull { it.value == value }
    }
}

object ModeSettings {
    const val MIN_ENABLED = 2

    fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences("se.ernie.soundmodetoggle.prefs", Context.MODE_PRIVATE)

    fun enabledModes(prefs: SharedPreferences): List<RingerMode> {
        val modes = RingerMode.entries.filter { prefs.getBoolean(it.prefKey, true) }
        // Fall back to the full cycle if the stored selection is somehow invalid
        return if (modes.size >= MIN_ENABLED) modes else RingerMode.entries
    }

    // The first enabled mode after the current one in cycle order. A mode that is not
    // enabled is skipped over, and an unknown mode counts as sitting just before Sound
    fun nextMode(current: Int, enabled: List<RingerMode>): RingerMode {
        val all = RingerMode.entries
        val start = all.indexOfFirst { it.value == current }
        return (1..all.size).map { all[(start + it) % all.size] }.first { it in enabled }
    }
}

package se.ernie.soundmodetoggle

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout

// Opened from the launcher icon's long-press menu or the system App info page
class SettingsActivity : Activity() {
    private lateinit var checkBoxes: Map<RingerMode, CheckBox>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val prefs = ModeSettings.prefs(this)
        val enabled = ModeSettings.enabledModes(prefs)
        val container = findViewById<LinearLayout>(R.id.modes)

        checkBoxes = RingerMode.entries.associateWith { mode ->
            CheckBox(this).apply {
                setText(mode.label)
                minHeight = (48 * resources.displayMetrics.density).toInt()
                isChecked = mode in enabled
                setOnCheckedChangeListener { _, isChecked ->
                    prefs.edit().putBoolean(mode.prefKey, isChecked).apply()
                    updateLockedModes()
                }
                container.addView(this)
            }
        }
        updateLockedModes()

        findViewById<Button>(R.id.done).setOnClickListener { finish() }
    }

    // Keep at least two modes in the cycle: once only two are checked they can't be unchecked
    private fun updateLockedModes() {
        val checkedCount = checkBoxes.values.count { it.isChecked }
        checkBoxes.values.forEach { it.isEnabled = !it.isChecked || checkedCount > ModeSettings.MIN_ENABLED }
    }
}

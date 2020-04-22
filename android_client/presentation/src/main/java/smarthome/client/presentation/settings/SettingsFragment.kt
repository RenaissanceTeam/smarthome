package smarthome.client.presentation.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import smarthome.client.presentation.R

class SettingsFragment : PreferenceFragmentCompat() {
    private val viewModel: SettingsViewModel by viewModels()
    private val homeServer by lazy { findPreference<Preference>("home_server") }
    private val signOut by lazy { findPreference<Preference>("sign_out") }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signOut?.setOnPreferenceClickListener { viewModel.signOut(); true }
    }
}
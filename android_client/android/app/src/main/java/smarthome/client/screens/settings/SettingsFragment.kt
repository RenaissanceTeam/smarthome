package smarthome.client.screens.settings

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import smarthome.client.R

class SettingsFragment : PreferenceFragmentCompat() {
    private val viewModel
            by lazy { ViewModelProviders.of(this).get(SettingsViewModel::class.java) }
    private val currentAccount by lazy { findPreference<Preference>("current_account") }
    private val signOut by lazy { findPreference<Preference>("sign_out") }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        viewModel.currentAccount.observe(this, Observer { currentAccount?.summary = it })
        signOut?.setOnPreferenceClickListener { viewModel.signOut(); true }
    }
}
package smarthome.client.presentation.settings

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import smarthome.client.presentation.R

class SettingsFragment : PreferenceFragmentCompat() {
    private val viewModel: SettingsViewModel by viewModels()
    private val currentAccount by lazy {
        findPreference<Preference>("current_account")
    } // todo to constants
    private val signOut by lazy { findPreference<Preference>("sign_out") }
    
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        
        viewModel.currentAccount.observe(viewLifecycleOwner) { currentAccount?.summary = it }
        signOut?.setOnPreferenceClickListener { viewModel.signOut(); true }
    }
}
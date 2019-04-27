package smarthome.client.viewpager.settings

import android.os.Bundle
import android.preference.PreferenceFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import smarthome.client.Model
import smarthome.client.R

class SettingsFragment : PreferenceFragmentCompat() {
    private val viewModel
            by lazy { ViewModelProviders.of(this).get(SettingsViewModel::class.java) }
    private val currentAccount by lazy { findPreference<Preference>("current_account") }
    private val signOut by lazy { findPreference<Preference>("sign_out") }
    private val doNotDisturb by lazy { findPreference<Preference>("do_not_disturb") }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        viewModel.currentAccount.observe(this, Observer { currentAccount?.summary = it })
        signOut?.setOnPreferenceClickListener { viewModel.signOut(); true }
        doNotDisturb?.setOnPreferenceChangeListener { _, newValue -> viewModel.changeDoNotDisturbMode(newValue as Boolean); true }
    }
}
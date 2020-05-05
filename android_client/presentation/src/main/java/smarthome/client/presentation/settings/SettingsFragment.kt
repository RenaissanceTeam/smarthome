package smarthome.client.presentation.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import smarthome.client.presentation.R

class SettingsFragment : PreferenceFragmentCompat() {
    private val viewModel: SettingsViewModel by viewModels()
    private val homeServer by lazy { findPreference<Preference>("home_server") }
    private val signOut by lazy { findPreference<Preference>("sign_out") }
    private val homeGeofence by lazy { findPreference<Preference>("home_geofence") }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycle.addObserver(viewModel)

        viewModel.toLogin.onNavigate(this) {
            findNavController().popBackStack(R.id.loginFragment, false)
        }

        viewModel.toHomeServer.onNavigate(this) {
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToHomeServerFragment())
        }

        viewModel.toHomeLocation.onNavigate(this) {
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToSetupHomeGeofenceFragment())
        }

        signOut?.setOnPreferenceClickListener { viewModel.onSignOut(); true }
        homeServer?.setOnPreferenceClickListener { viewModel.onChangeHomeServer(); true }
        homeGeofence?.setOnPreferenceClickListener { viewModel.onSetupHomeLocation(); true }
    }
}
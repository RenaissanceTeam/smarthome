package smarthome.client.presentation.settings

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.koin.core.inject
import smarthome.client.domain.api.auth.usecases.LogoutUseCase
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.util.NavigationLiveData

class SettingsViewModel : KoinViewModel() {
    val logoutUseCase: LogoutUseCase by inject()
    val toLogin = NavigationLiveData()
    val toHomeServer = NavigationLiveData()
    val toHomeLocation = NavigationLiveData()

    fun onSignOut() = viewModelScope.launch {
        logoutUseCase.execute()
        toLogin.trigger()
    }

    fun onChangeHomeServer() {
        toHomeServer.trigger()
    }

    fun onSetupHomeLocation() {
        toHomeLocation.trigger()
    }
}
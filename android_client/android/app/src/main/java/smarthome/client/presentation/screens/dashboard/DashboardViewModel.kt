package smarthome.client.presentation.screens.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import smarthome.client.domain.usecases.AuthenticationUseCase
import smarthome.client.domain.usecases.DevicesUseCase
import smarthome.library.common.IotDevice

class DashboardViewModel : ViewModel(), KoinComponent {
    val TAG = DashboardViewModel::class.java.simpleName

    private val _devices = MutableLiveData<MutableList<IotDevice>>()
    private val _allHomeUpdateState = MutableLiveData<Boolean>()
    private val _toastMessage = MutableLiveData<String?>()

    private val devicesUseCase: DevicesUseCase by inject()
    private val authenticationUseCase: AuthenticationUseCase by inject()

    private var devicesSubscription: Disposable? = null
    private val authSubscription: Disposable

    val devices: LiveData<MutableList<IotDevice>>
        get() = _devices

    val allHomeUpdateState: LiveData<Boolean>
        get() = _allHomeUpdateState

    val toastMessage: LiveData<String?>
        get() = _toastMessage

    init {
        authSubscription = authenticationUseCase.getAuthenticationStatus().subscribe {
            if (it) {
                requestSmartHomeState()
            } else {
                devicesSubscription?.dispose()
                devicesSubscription = null
                // todo show user not logged in placeholder
            }
        }
    }

    fun requestSmartHomeState() {
        viewModelScope.launch {
            _allHomeUpdateState.value = true

            devicesSubscription = devicesUseCase.getDevices().doOnError {
                _toastMessage.value = "Error $it"
            }.subscribe {
                _devices.value = it
                _allHomeUpdateState.value = false
            }
        }
    }

    fun toastShowed() { _toastMessage.value = null }

    override fun onCleared() {
        super.onCleared()
        devicesSubscription?.dispose()
    }
}
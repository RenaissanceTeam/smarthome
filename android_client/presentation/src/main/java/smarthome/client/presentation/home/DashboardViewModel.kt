package smarthome.client.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import smarthome.client.domain.api.entity.Device
import smarthome.client.domain.api.usecase.AuthenticationUseCase
import smarthome.client.domain.api.usecase.DevicesUseCase


class DashboardViewModel(
    private val devicesUseCase: DevicesUseCase,
    private val authenticationUseCase: AuthenticationUseCase
) : ViewModel() {
    private val _devices = MutableLiveData<MutableList<Device>>()
    private val _allHomeUpdateState = MutableLiveData<Boolean>()
    private val _toastMessage = MutableLiveData<String?>()

    private var devicesSubscription: Disposable? = null
    private val authSubscription: Disposable

    val devices: LiveData<MutableList<Device>>
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
                _toastMessage.value = "Not logged in"
                // todo show user not logged in placeholder
            }
        }
    }

    fun requestSmartHomeState() {
        viewModelScope.launch {
            _allHomeUpdateState.value = true

            devicesSubscription = devicesUseCase.getDevices()
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError { _toastMessage.value = "Error $it" }
                    .subscribe {
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
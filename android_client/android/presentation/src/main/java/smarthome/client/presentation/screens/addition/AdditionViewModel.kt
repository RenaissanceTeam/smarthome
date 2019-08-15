package smarthome.client.presentation.screens.addition

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import smarthome.client.domain.domain.usecases.AuthenticationUseCase
import smarthome.client.domain.domain.usecases.PendingDevicesUseCase
import smarthome.library.common.BaseController
import smarthome.library.common.DeviceServeState
import smarthome.library.common.IotDevice

class AdditionViewModel : ViewModel(), KoinComponent {
    private val _devices = MutableLiveData<MutableList<IotDevice>>()
    private val _globalUpdateState = MutableLiveData<Boolean>()
    private var devicesSubscription: Disposable? = null
    private val authSubscription: Disposable
    private val authenticationUseCase: smarthome.client.domain.domain.usecases.AuthenticationUseCase by inject()
    private val pendingDevicesUseCase: smarthome.client.domain.domain.usecases.PendingDevicesUseCase by inject()
    var viewNotifier: ViewNotifier? = null
    val devices: LiveData<MutableList<IotDevice>>
        get() = _devices

    val globalUpdateState: LiveData<Boolean>
        get() = _globalUpdateState

    init {
        authSubscription = authenticationUseCase.getAuthenticationStatus()
                .subscribe { if (it) requestSmartHomeState(); }
    }

    fun requestSmartHomeState() {
        viewModelScope.launch {
            _globalUpdateState.value = true

            devicesSubscription = pendingDevicesUseCase.getPendingDevices().subscribe {
                _devices.value?.let { old ->
                    // todo change to list adapter
                    if (old.size > it.size) {
                        if (it.size == 0) {
                            viewNotifier?.onItemRemoved(0)
                            return@let
                        }

                        for (i in 0..it.size) {
                            if (old[i] != it[i]) {
                                viewNotifier?.onItemRemoved(i)
                                return@let
                            }
                        }

                        viewNotifier?.onItemRemoved(old.size - 1)
                    }
                }

                _devices.value = it
                _globalUpdateState.value = false
            }
        }
    }

    fun onControllerChanged(controller: BaseController?) {
        controller ?: return

        viewModelScope.launch {
            val device = pendingDevicesUseCase.findPendingDevice(controller)
//            device.controllers[device.controllers.indexOf(controller)] = controller
            pendingDevicesUseCase.changePendingDevice(device)
        }
    }

    override fun onCleared() {
        super.onCleared()
        devicesSubscription?.dispose()
    }

    fun acceptDevice(device: IotDevice?) {
        device ?: return

        device.serveState = DeviceServeState.ACCEPT_PENDING_TO_ADD
        viewModelScope.launch { pendingDevicesUseCase.changePendingDevice(device) }
    }

    fun rejectDevice(device: IotDevice?) {
        device ?: return

        device.serveState = DeviceServeState.DELETE
        viewModelScope.launch { pendingDevicesUseCase.changePendingDevice(device) }
    }
}
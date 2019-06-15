package smarthome.client.presentation.screens.addition

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import smarthome.client.BuildConfig
import smarthome.client.domain.usecases.AuthenticationUseCase
import smarthome.client.domain.usecases.PendingDevicesUseCase
import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice

class AdditionViewModel : ViewModel(), KoinComponent {

    val TAG = javaClass.name

    var viewNotifier: ViewNotifier? = null

    private val _devices = MutableLiveData<MutableList<IotDevice>>()
    private val _globalUpdateState = MutableLiveData<Boolean>()
    private val _toastMessage = MutableLiveData<String?>()

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)
    private var devicesSubscription: Disposable? = null
    private val authSubscription: Disposable

    private val authenticationUseCase: AuthenticationUseCase by inject()
    private val pendingDevicesUseCase: PendingDevicesUseCase by inject()


    val devices: LiveData<MutableList<IotDevice>>
        get() = _devices

    val allHomeUpdateState: LiveData<Boolean>
        get() = _globalUpdateState

    val toastMessage: LiveData<String?>
        get() = _toastMessage


    init {
        authSubscription = authenticationUseCase.getAuthenticationStatus().subscribe { if (it) requestSmartHomeState(); }
    }

    fun requestSmartHomeState() {
        if (BuildConfig.DEBUG) Log.d(TAG, "request smart home state")

        uiScope.launch {
            _globalUpdateState.value = true
            try {
                devicesSubscription = pendingDevicesUseCase.getPendingDevices().subscribe {
                    _devices.value?.let {old -> // todo change to list adapter
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
            } catch (e: Throwable) {
                _toastMessage.value = "Can't listen for devices update"
                if (BuildConfig.DEBUG) Log.d(TAG, "", e)
            }
        }
    }


    fun onControllerChanged(controller: BaseController?) {
        controller ?: return
        viewModelScope.launch {
            val device = pendingDevicesUseCase.findPendingDevice(controller)
            device.controllers[device.controllers.indexOf(controller)] = controller
            pendingDevicesUseCase.changePendingDevice(device)
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
        devicesSubscription?.dispose()
    }

    fun acceptDevice(device: IotDevice?) {
        device ?: return

        device.setAccepted()
        viewModelScope.launch { pendingDevicesUseCase.changePendingDevice(device) }
    }

    fun rejectDevice(device: IotDevice?) {
        device ?: return

        device.setRejected()
        viewModelScope.launch { pendingDevicesUseCase.changePendingDevice(device) }
    }
}
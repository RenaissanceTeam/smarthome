package smarthome.client.viewpager.addition

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import smarthome.client.BuildConfig
import smarthome.client.HomeModelException
import smarthome.client.Model
import smarthome.client.auth.Authenticator
import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice

class AdditionViewModel : ViewModel() {

    val TAG = javaClass.name

    var viewNotifier: ViewNotifier? = null

    private val _devices = MutableLiveData<MutableList<IotDevice>>()
    private val _allHomeUpdateState = MutableLiveData<Boolean>()
    private val _toastMessage = MutableLiveData<String?>()

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)
    private var devicesSubscription: Disposable? = null
    private val authSubscription: Disposable

    val devices: LiveData<MutableList<IotDevice>>
        get() = _devices

    val allHomeUpdateState: LiveData<Boolean>
        get() = _allHomeUpdateState

    val toastMessage: LiveData<String?>
        get() = _toastMessage


    init {
        authSubscription = Authenticator.isAuthenticated.subscribe { if (it) requestSmartHomeState(); }
    }

    fun requestSmartHomeState() {
        if (BuildConfig.DEBUG) Log.d(TAG, "request smart home state")

        uiScope.launch {
            _allHomeUpdateState.value = true
            if (devicesSubscription == null) tryListenForUpdates()
            try {
                _devices.value = Model.getPendingDevicesCopy()
            } catch (e: HomeModelException) {
                if (BuildConfig.DEBUG) Log.d(TAG, "request home state failed", e)
            }
            _allHomeUpdateState.value = false
        }
    }

    private suspend fun tryListenForUpdates() {
        try {
            devicesSubscription = Model.getPendingDevicesObservable().subscribe {
                _devices.value?.let {old ->
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
                _allHomeUpdateState.value = false
            }
        } catch (e: Throwable) {
            _toastMessage.value = "Can't listen for devices update"
            if (BuildConfig.DEBUG) Log.d(TAG, "", e)
        }
    }

    suspend fun onControllerChanged(controller: BaseController) {
        val device = Model.getPendingDevice(controller)
        device.controllers[device.controllers.indexOf(controller)] = controller
        Model.changePendingDevice(device)
    }

    private fun updateDevice(device: IotDevice) {
        uiScope.launch {
            //_refresh.value = true
            Model.changePendingDevice(device)
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
        devicesSubscription?.dispose()
    }

}
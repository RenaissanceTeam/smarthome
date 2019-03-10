package smarthome.client.viewpager.dashboard

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import smarthome.client.BuildConfig
import smarthome.client.HomeModelException
import smarthome.client.Model
import smarthome.client.NoDeviceException
import smarthome.library.common.BaseController
import smarthome.library.common.ControllerType
import smarthome.library.common.IotDevice

class DashboardViewModel : ViewModel() {
    val TAG = DashboardViewModel::class.java.simpleName

    private val _devices = MutableLiveData<MutableList<IotDevice>>()
    private val _allHomeUpdateState = MutableLiveData<Boolean>()

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)
    init {
        requestSmartHomeState()
    }

    val devices: LiveData<MutableList<IotDevice>>
        get() = _devices

    val allHomeUpdateState: LiveData<Boolean>
        get() = _allHomeUpdateState

    fun requestSmartHomeState() {
        if (BuildConfig.DEBUG) Log.d(TAG, "request smart home state")
        uiScope.launch {
            _allHomeUpdateState.value = true
            try {
                _devices.value = Model.getDevices()
            } catch (e: HomeModelException) {
                // todo find a way to notify user about error
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun receivedNewSmartHomeState() {
        if (BuildConfig.DEBUG) Log.d(TAG, "set refreshing state to false")
        _allHomeUpdateState.value = false
    }
}
package smarthome.client.viewpager.dashboard

import android.util.Log
import androidx.lifecycle.*
import smarthome.client.BuildConfig
import smarthome.client.Model
import smarthome.library.common.BaseController
import smarthome.library.common.ControllerType
import smarthome.library.common.IotDevice

class DashboardViewModel : ViewModel() {
    val TAG = DashboardViewModel::class.java.simpleName

    private val _devices = MutableLiveData<MutableList<IotDevice>>()
    private val _allHomeUpdateState = MutableLiveData<Boolean>()


    val devices: LiveData<MutableList<IotDevice>>
        get() = _devices

    val allHomeUpdateState: LiveData<Boolean>
        get() = _allHomeUpdateState

    fun requestSmartHomeState() {
        if (BuildConfig.DEBUG) Log.d(TAG, "request smart home state")
        _allHomeUpdateState.value = true
        Model.getDevices { _devices.value = it; }
    }

    fun receivedNewSmartHomeState() {
        if (BuildConfig.DEBUG) Log.d(TAG, "set refreshing state to false")
        _allHomeUpdateState.value = false
    }
}
package smarthome.client.dashboard

import android.util.Log
import androidx.lifecycle.*
import smarthome.client.BuildConfig
import smarthome.client.Model
import smarthome.library.common.BaseController
import smarthome.library.common.ControllerType
import smarthome.library.common.IotDevice

class DashboardViewModel : ViewModel() {
    val TAG = DashboardViewModel::class.java.simpleName

    private val _devices = Model.getHomeState()
    private val _allHomeUpdateState = MutableLiveData<Boolean>()


    val devices: LiveData<MutableList<IotDevice>>
        get() = _devices

    val allHomeUpdateState: LiveData<Boolean>
        get() = _allHomeUpdateState

    fun requestSmartHomeState() {
        if (BuildConfig.DEBUG) Log.d(TAG, "request smart home state")
        _allHomeUpdateState.value = true
        Model.requestHomeStateFromRaspberry()
    }

    fun receivedNewSmartHomeState() {
        if (BuildConfig.DEBUG) Log.d(TAG, "set refreshing state to false")
        _allHomeUpdateState.value = false
    }

    fun clickOnController(controller: BaseController) {
        _allHomeUpdateState.value = true
        if (controller.type == ControllerType.ARDUINO_ON_OFF) {
            when (controller.state) {
                "0" -> changeStateTo(controller, "1")
                "1" -> changeStateTo(controller, "0")
            }
        } else {
            readState(controller)
        }
    }

    private fun readState(controller: BaseController) {
// todo        startStateChange()
        Model.readController(controller)
    }

    private fun changeStateTo(controller: BaseController, value: String) {
// todo        startStateChange()
        Model.changeControllerState(controller, value)
    }

    fun getDevice(controller: BaseController?) : IotDevice? {
        if (controller == null) return null
        return Model.getDevice(controller)
    }
}
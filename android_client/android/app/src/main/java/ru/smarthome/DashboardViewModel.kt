package ru.smarthome

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.smarthome.library.BaseController
import ru.smarthome.library.ControllerType
import ru.smarthome.library.IotDevice

class DashboardViewModel : ViewModel() {
    val TAG = DashboardViewModel::class.java.simpleName
    val devices = mutableListOf<IotDevice>()
    val controllersCount
        get() = controllers.value?.size ?: 0


    var controllers = MutableLiveData<MutableList<BaseController>>()
    var allHomeUpdateState = MutableLiveData<Boolean>()
    var refreshingState = MutableLiveData<Boolean>()

    fun requestSmartHomeState() {
        refreshingState.value = true
        controllers = Model.requestHomeStateFromRaspberry()
    }

    fun receivedNewSmartHomeState() {
        refreshingState.value = false
    }

    fun clickOnController(controller: BaseController) {
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
        controllers = Model.readController(controller) // returns livedata, when fetching finished, this object will be notified of update
    }

    private fun changeStateTo(controller: BaseController, value: String) {
// todo        startStateChange()
        controllers = Model.changeControllerState(controller, value)
    }

    fun getDevice(position: Int) : IotDevice? {
        val controller = getController(position) ?: return null
        return Model.getDevice(controller)
    }

    fun getController(position: Int): BaseController? {
        return controllers.value?.get(position)
    }
}
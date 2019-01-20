package ru.smarthome.dashboard

import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import ru.smarthome.BuildConfig
import ru.smarthome.Model
import ru.smarthome.auth.Authenticator
import ru.smarthome.library.BaseController
import ru.smarthome.library.ControllerType
import ru.smarthome.library.IotDevice

class DashboardViewModel : ViewModel() {
    val TAG = DashboardViewModel::class.java.simpleName
    val controllersCount
        get() = controllers.value?.size ?: 0

    private val _controllers = Model.getHomeState()
    private val _allHomeUpdateState = MutableLiveData<Boolean>()


    val controllers: LiveData<MutableList<BaseController>>
        get() = _controllers

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

    fun getDevice(position: Int) : IotDevice? {
        val controller = getController(position) ?: return null
        return Model.getDevice(controller)
    }

    fun getController(position: Int): BaseController? {
        return controllers.value?.get(position)
    }
}
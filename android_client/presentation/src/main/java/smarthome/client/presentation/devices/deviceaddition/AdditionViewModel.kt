package smarthome.client.presentation.devices.deviceaddition

import androidx.lifecycle.MutableLiveData
import smarthome.client.entity.Controller
import smarthome.client.entity.Device
import smarthome.client.presentation.util.KoinViewModel

class AdditionViewModel : KoinViewModel() {
    private val devices = MutableLiveData<MutableList<Device>>()
    var viewNotifier: ViewNotifier? = null
    
    fun onControllerChanged(controller: Controller) {
        TODO()
    }
    
    
    fun acceptDevice(device: Device?) {
        TODO()
    }
    
    fun rejectDevice(device: Device?) {
        TODO()
    }
}
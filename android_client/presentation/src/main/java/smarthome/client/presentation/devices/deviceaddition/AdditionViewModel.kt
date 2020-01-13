package smarthome.client.presentation.devices.deviceaddition

import androidx.lifecycle.MutableLiveData
import smarthome.client.entity.Controller
import smarthome.client.entity.Device
import smarthome.client.presentation.util.KoinViewModel

class AdditionViewModel : KoinViewModel() {
    val devices = MutableLiveData<MutableList<Device>>()
    
    fun onControllerChanged(controller: Controller) {
        TODO()
    }
    
    fun acceptDevice(device: Device?) {
        TODO()
    }
    
    fun rejectDevice(device: Device?) {
        TODO()
    }
    
    fun onDeviceClicked(id: Long) {
        TODO()
    }
    
    fun onControllerClicked(id: Long) {
        TODO()
    }
    
    fun onAddDeviceClicked() {
        TODO()
    }
}
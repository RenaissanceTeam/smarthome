package smarthome.client.presentation.devices.deviceaddition

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import org.koin.core.KoinComponent
import smarthome.client.domain_api.entity.Controller
import smarthome.client.domain_api.entity.Device

class AdditionViewModel : ViewModel(), KoinComponent {
    private val _devices = MutableLiveData<MutableList<Device>>()
    private var devicesSubscription: Disposable? = null
    var viewNotifier: ViewNotifier? = null
    val devices: LiveData<MutableList<Device>>
        get() = _devices
    
    fun onControllerChanged(controller: Controller) {
        TODO()
    }

    override fun onCleared() {
        super.onCleared()
        devicesSubscription?.dispose()
    }

    fun acceptDevice(device: Device?) {
        TODO()
    }

    fun rejectDevice(device: Device?) {
        TODO()
    }
}
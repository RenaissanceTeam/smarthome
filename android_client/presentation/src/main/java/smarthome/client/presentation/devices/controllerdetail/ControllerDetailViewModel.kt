package smarthome.client.presentation.devices.controllerdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.launch
import smarthome.client.domain.api.entity.Controller
import smarthome.client.domain.api.entity.Device
import smarthome.client.domain.api.usecase.ControllersUseCase
import smarthome.client.domain.api.usecase.DevicesUseCase
import smarthome.client.presentation.NoControllerException
import smarthome.client.presentation.NoDeviceWithControllerException
import smarthome.client.presentation.devices.controllerdetail.statechanger.StateChangerType


class ControllerDetailViewModel(
    private val controllersUseCase: ControllersUseCase,
    private val devicesUseCase: DevicesUseCase
) : ViewModel() {
    private val _refresh = MutableLiveData<Boolean>()
    val refresh: LiveData<Boolean>
        get() = _refresh

    private val _controller = MutableLiveData<Controller>()
    val controller: LiveData<Controller>
        get() = _controller

    private val _device = MutableLiveData<Device>()
    val device: LiveData<Device>
        get() = _device

    private val _stateChangerType = MutableLiveData<StateChangerType>()
    val stateChangerType: LiveData<StateChangerType>
        get() = _stateChangerType

    private var disposable: Disposable? = null

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }

    fun controllerNameChanged(name: String) {
        TODO()
    }
}
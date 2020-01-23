package smarthome.client.presentation.scripts.addition.graph.controllers

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import org.koin.core.inject
import smarthome.client.domain.api.conrollers.usecases.ObserveControllerUseCase
import smarthome.client.domain.api.devices.dto.GeneralDeviceInfo
import smarthome.client.domain.api.devices.usecase.GetGeneralDevicesInfo
import smarthome.client.entity.Controller
import smarthome.client.presentation.runInScopeCatchingAny
import smarthome.client.presentation.scripts.addition.graph.controllers.epoxy.DeviceItemState
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.util.DataStatus
import smarthome.client.util.log

class ControllersViewViewModel : KoinViewModel() {
    
    val dragging = MutableLiveData<Float>()
    val animateTo = MutableLiveData<Float>()
    val jumpTo = MutableLiveData<Float>()
    val devices = MutableLiveData<List<DeviceItemState>>()
    val controllers = mutableMapOf<Long, DataStatus<Controller>>()
    
    private val getGeneralDeviceInfo: GetGeneralDevicesInfo by inject()
    private val observeController: ObserveControllerUseCase by inject()
    private var currentSlide = 0f
    private var startSlide = 0f
    private var currentRelativeDragProgress = 0f
    private var width = 0f
    private var waitingForMove = false
    private val onFirstShow by lazy { fetchDevices() }
    
    fun open() {
        animateTo.value = 0f
        onFirstShow
    }
    
    fun onActionDown(at: Float, x: Float): Boolean {
        if (x/width > LEFT_SIDE_PERCENT) {
            waitingForMove = false
            return false
        }
        
        waitingForMove = true
        startSlide = at
        currentSlide = at
        currentRelativeDragProgress = 0f
        return true
    }
    
    fun moveTo(newX: Float): Boolean {
        if (!waitingForMove) return false
        
        val delta = newX - currentSlide
        currentSlide = newX
        
        currentRelativeDragProgress = minOf(maxOf(currentRelativeDragProgress + delta, 0f), width)
        dragging.value = currentRelativeDragProgress
        return true
    }
    
    fun setWidth(width: Float) {
        this.width = width
    }
    
    fun onActionUp(): Boolean {
        if (!waitingForMove) return false
    
        when (currentRelativeDragProgress/width > 0.5) {
            true -> animateToClose()
            false -> animateToOpen()
        }
        return true
    }
    
    private fun animateToOpen() {
        animateTo.value = 0f
    }
    
    private fun animateToClose() {
        animateTo.value = width
    }
    
    fun onInit() {
        jumpTo.value = width
    }
    
    private fun fetchDevices() {
        log("fetching devices")
        getGeneralDeviceInfo.runInScopeCatchingAny(viewModelScope) {
            val devices = execute()
            devices.flatMap { it.controllers }.forEach { id ->
                disposable.add(observeController.execute(id).subscribe {
                    controllers[id] = it
                })
            }
            this@ControllersViewViewModel.devices.value = devices.map(::deviceToDeviceItemState)
        }
    }
    
    private fun deviceToDeviceItemState(device: GeneralDeviceInfo): DeviceItemState {
        return DeviceItemState(device.id, device.name, device.controllers)
    }
    
    companion object {
        const val LEFT_SIDE_PERCENT = 0.3
    }
}

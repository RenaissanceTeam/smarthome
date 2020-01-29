package smarthome.client.presentation.scripts.addition.controllers

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import org.koin.core.inject
import smarthome.client.domain.api.conrollers.usecases.ObserveControllerUseCase
import smarthome.client.domain.api.devices.dto.GeneralDeviceInfo
import smarthome.client.domain.api.devices.usecase.GetGeneralDevicesInfo
import smarthome.client.entity.Controller
import smarthome.client.presentation.runInScopeCatchingAny
import smarthome.client.presentation.scripts.addition.controllers.epoxy.DeviceItemState
import smarthome.client.presentation.scripts.addition.graph.ControllerGraphBlockIdentifier
import smarthome.client.presentation.scripts.addition.graph.DragOperationInfo
import smarthome.client.presentation.scripts.addition.graph.Position
import smarthome.client.presentation.scripts.addition.graph.events.GraphEventBus
import smarthome.client.presentation.scripts.addition.graph.events.drag.EndDrag
import smarthome.client.presentation.scripts.addition.graph.events.drag.StartDrag
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.util.DataStatus

class ControllersViewViewModel : KoinViewModel() {
    
    val jumpTo = MutableLiveData<Float>()
    val animateTo = MutableLiveData<Float>()
    val devices = MutableLiveData<List<DeviceItemState>>()
    val controllers = mutableMapOf<Long, DataStatus<Controller>>()
    
    private val observingController = mutableMapOf<Long, Boolean>()
    private val hiddenControllers = mutableMapOf<Long, Boolean>()
    private val getGeneralDeviceInfo: GetGeneralDevicesInfo by inject()
    private val observeController: ObserveControllerUseCase by inject()
    private val graphEventBus: GraphEventBus by inject()
    private var currentSlide = 0f
    private var startSlide = 0f
    private var currentRelativeDragProgress = 0f
    private var width = 0f
    private var waitingForMove = false
    private val onFirstShow by lazy { fetchDevices() }
    
    init {
        disposable.add(graphEventBus.observe()
            .filter {
                true
                // it is drag event and it is related to controllers hub
            }
            .subscribe {
                when (it) {
                    is StartDrag -> {
                        // hide dragged view
                    }
                    is EndDrag -> {
                        // add this block to controllers (and start observing)
                    }
                }
            })
    }
    
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
        jumpTo.value = currentRelativeDragProgress
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
        getGeneralDeviceInfo.runInScopeCatchingAny(viewModelScope) {
            val devices = execute()
            devices.flatMap { it.controllers }.forEach { id ->
                if (observingController[id] == true) return@forEach
    
                observingController[id] = true
                disposable.add(observeController.execute(id).subscribe {
                    controllers[id] = it
                    triggerDevicesRebuildModels()
                })
            }
            this@ControllersViewViewModel.devices.value = devices.map(::deviceToDeviceItemState)
        }
    }
    
    private fun deviceToDeviceItemState(device: GeneralDeviceInfo): DeviceItemState {
        return DeviceItemState(device.id, device.name, device.controllers)
    }
    
    fun onDragStarted(id: Long, dragTouch: Position): DragOperationInfo {
        hiddenControllers[id] = true
        triggerDevicesRebuildModels()
        return DragOperationInfo("controllersHub", dragTouch, "controller",
            ControllerGraphBlockIdentifier(id)) { droppedTo ->
            when (droppedTo) {
                "controllersHub" -> {
                    onDragCancelled(id)
                    true
                }
                else -> false
            }
        }
    }
    
    private fun onDragCancelled(id: Long) {
        hiddenControllers[id] = false
        triggerDevicesRebuildModels()
    }
    
    fun shouldShow(id: Long): Boolean {
        return hiddenControllers[id]?.not() ?: DEFAULT_SHOW
    }
    
    private fun triggerDevicesRebuildModels() {
        devices.value = devices.value ?: return
    }
    
    companion object {
        const val LEFT_SIDE_PERCENT = 0.3
        const val DEFAULT_SHOW = true
    }
}

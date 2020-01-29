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
import smarthome.client.presentation.scripts.addition.graph.Position
import smarthome.client.presentation.scripts.addition.graph.events.GraphEventBus
import smarthome.client.presentation.scripts.addition.graph.events.drag.*
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.util.DataStatus

class ControllersHubViewModel : KoinViewModel() {
    
    val devices = MutableLiveData<List<DeviceItemState>>()
    val controllers = mutableMapOf<Long, DataStatus<Controller>>()
    
    private val observingController = mutableMapOf<Long, Boolean>()
    private val hiddenControllers = mutableMapOf<Long, Boolean>()
    private val getGeneralDeviceInfo: GetGeneralDevicesInfo by inject()
    private val observeController: ObserveControllerUseCase by inject()
    private val graphEventBus: GraphEventBus by inject()
    private var width = 0f
    private val onFirstShow by lazy { fetchDevices() }
    
    init {
        disposable.add(graphEventBus.observe()
            .filter {
                it is ControllerDragEvent
                    && (it.isFromOrTo(CONTROLLERS_HUB))
            }
            .map { it as ControllerDragEvent }
            .subscribe { event ->
                when (event.dragInfo.status) {
                    DRAG_START -> if (event.isFrom(CONTROLLERS_HUB)) hideController(event.id)
                    DRAG_DROP -> if (event.isTo(CONTROLLERS_HUB)) handleDroppedController(event.id)
                }
            })
    }
    
    private fun handleDroppedController(id: Long) {
        hiddenControllers[id] = false
        startObservingController(id)
    }
    
    fun onDropped(drag: GraphDragEvent) {
        if (drag is ControllerDragEvent) {
            val dropInfo = drag.dragInfo.copy(to = CONTROLLERS_HUB, status = DRAG_DROP)
            val dropEvent = drag.copy(dragInfo = dropInfo)
            
            graphEventBus.addEvent(dropEvent)
        } else {
            val cancelInfo = drag.dragInfo.copy(to = CONTROLLERS_HUB, status = DRAG_CANCEL)
            val cancelEvent = drag.copyWithDragInfo(cancelInfo)
    
            graphEventBus.addEvent(cancelEvent)
        }
    }
    
    fun open() {
        onFirstShow
    }
    
    private fun hideController(id: Long) {
        hiddenControllers[id] = true
        triggerDevicesRebuildModels()
    }
    
    private fun fetchDevices() {
        getGeneralDeviceInfo.runInScopeCatchingAny(viewModelScope) {
            val devices = execute()
            devices.flatMap { it.controllers }.forEach(::startObservingController)
            this@ControllersHubViewModel.devices.value = devices.map(::deviceToDeviceItemState)
        }
    }
    
    private fun startObservingController(id: Long) {
        if (observingController[id] == true) return
    
        observingController[id] = true
        disposable.add(observeController.execute(id).subscribe {
            controllers[id] = it
            triggerDevicesRebuildModels()
        })
    }
    
    private fun deviceToDeviceItemState(device: GeneralDeviceInfo): DeviceItemState {
        return DeviceItemState(device.id, device.name, device.controllers)
    }
    
    fun onDragStarted(id: Long, dragTouch: Position): ControllerDragEvent {
        return ControllerDragEvent(
            id = id,
            dragInfo = CommonDragInfo(
                status = DRAG_START,
                dragTouch = dragTouch,
                from = CONTROLLERS_HUB
            )
        ).also(graphEventBus::addEvent)
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

package smarthome.client.presentation.scripts.setup.controllers

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import org.koin.core.inject
import smarthome.client.domain.api.conrollers.usecases.ObserveControllerUseCase
import smarthome.client.domain.api.devices.dto.GeneralDeviceInfo
import smarthome.client.domain.api.devices.usecase.GetGeneralDevicesInfo
import smarthome.client.entity.Controller
import smarthome.client.entity.script.block.BlockId
import smarthome.client.entity.script.controller.ControllerBlockId
import smarthome.client.presentation.scripts.setup.controllers.epoxy.DeviceItemState
import smarthome.client.presentation.scripts.setup.graph.events.GraphEventBus
import smarthome.client.presentation.scripts.setup.graph.events.drag.*
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.util.DataStatus
import smarthome.client.util.Position
import smarthome.client.util.runInScopeCatchingAny

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
                it is BlockDragEvent
                    && (it.isFromOrTo(CONTROLLERS_HUB))
            }
            .map { it as BlockDragEvent }
            .subscribe { event ->
                when (event.dragInfo.status) {
                    DRAG_START -> if (event.isFrom(CONTROLLERS_HUB)) hideController(event.dragInfo.id)
                    DRAG_DROP -> if (event.isTo(CONTROLLERS_HUB)) handleDroppedController(event.dragInfo.id)
                }
            })
    }
    
    private fun handleDroppedController(id: BlockId) {
        if (id !is ControllerBlockId) return
        
        hiddenControllers[id.id] = false
        startObservingController(id.id)
        triggerDevicesRebuildModels()
    }
    
    fun onDropped(drag: BlockDragEvent) {
        val dropInfo = drag.dragInfo.copy(to = CONTROLLERS_HUB, status = DRAG_DROP)
        val dropEvent = drag.copy(dragInfo = dropInfo)
        
        graphEventBus.addEvent(dropEvent)
    }
    
    fun open() {
        onFirstShow
    }
    
    private fun hideController(id: BlockId) {
        if (id !is ControllerBlockId) return
        hiddenControllers[id.id] = true
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
    
    fun onDragStarted(id: Long, dragTouch: Position): BlockDragEvent {
        return BlockDragEvent(
            dragInfo = CommonDragInfo(
                id = ControllerBlockId(id),
                status = DRAG_START,
                dragTouch = dragTouch,
                from = CONTROLLERS_HUB
            )
        ).also(graphEventBus::addEvent)
    }
    
    fun shouldShow(id: Long) = hiddenControllers[id]?.not() ?: DEFAULT_SHOW
    
    private fun triggerDevicesRebuildModels() {
        devices.value = devices.value ?: return
    }
    
    companion object {
        const val LEFT_SIDE_PERCENT = 0.3
        const val DEFAULT_SHOW = true
    }
}

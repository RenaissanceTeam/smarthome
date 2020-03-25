package smarthome.client.presentation.scripts.setup.controllers

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import org.koin.core.inject
import smarthome.client.domain.api.conrollers.usecases.ObserveControllerUseCase
import smarthome.client.domain.api.devices.dto.GeneralDeviceInfo
import smarthome.client.domain.api.devices.usecase.GetGeneralDevicesInfo
import smarthome.client.domain.api.scripts.usecases.setup.GetBlockUseCase
import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.controller.ControllerBlock
import smarthome.client.presentation.scripts.setup.controllers.epoxy.ControllerState
import smarthome.client.presentation.scripts.setup.controllers.epoxy.DeviceItemState
import smarthome.client.presentation.scripts.setup.graph.events.GraphEventBus
import smarthome.client.presentation.scripts.setup.graph.events.drag.BlockDragInfo
import smarthome.client.presentation.scripts.setup.graph.events.drag.CONTROLLERS_HUB
import smarthome.client.presentation.scripts.setup.graph.events.drag.DRAG_DROP
import smarthome.client.presentation.scripts.setup.graph.events.drag.DRAG_START
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.util.Position
import smarthome.client.util.runInScopeCatchingAny

class ControllersHubViewModel : KoinViewModel() {
    
    val devices = MutableLiveData<List<DeviceItemState>>()
    val controllers = mutableMapOf<Long, ControllerState>()
    
    private val observingController = mutableMapOf<Long, Boolean>()
    private val hiddenControllers = mutableMapOf<Long, Boolean>()
    private val getGeneralDeviceInfo: GetGeneralDevicesInfo by inject()
    private val getBlockUseCase: GetBlockUseCase by inject()
    private val observeController: ObserveControllerUseCase by inject()
    private val graphEventBus: GraphEventBus by inject()
    private var width = 0f
    private val onFirstShow by lazy { fetchDevices() }
    
    init {
        disposable.add(graphEventBus.observe()
            .filter { it is BlockDragInfo && (it.isFromOrTo(CONTROLLERS_HUB)) }
            .map { it as BlockDragInfo }
            .subscribe { event ->
                when (event.status) {
                    DRAG_START -> if (event.isFrom(CONTROLLERS_HUB)) hideController(event.block)
                    DRAG_DROP -> if (event.isTo(CONTROLLERS_HUB)) handleDroppedController(event.block)
                }
            })
    }
    
    private fun handleDroppedController(block: Block) {
        if (block !is ControllerBlock) return
        
        hiddenControllers[block.controllerId] = false
        startObservingController(block.controllerId)
        triggerDevicesRebuildModels()
    }
    
    fun onDropped(drag: BlockDragInfo) {
        graphEventBus.addEvent(drag.copy(to = CONTROLLERS_HUB, status = DRAG_DROP))
    }
    
    fun open() {
        onFirstShow
    }
    
    private fun hideController(block: Block) {
        if (block !is ControllerBlock) return
        
        hiddenControllers[block.controllerId] = true
        
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
            controllers[id] = controllers[id]?.copy(controller = it) ?: ControllerState(it)
            triggerDevicesRebuildModels()
        })
    }
    
    private fun deviceToDeviceItemState(device: GeneralDeviceInfo): DeviceItemState {
        return DeviceItemState(device.id, device.name, device.controllers)
    }
    
    fun onDragStarted(id: Long, dragTouch: Position): BlockDragInfo {

        return (
            dragInfo = CommonDragInfo(
                block = ControllerBLock(id),
                status = DRAG_START,
                dragTouch = dragTouch,
                from = CONTROLLERS_HUB
            )
        ).also(graphEventBus::addEvent)
    }
    
    private fun triggerDevicesRebuildModels() {
        devices.value = devices.value ?: return
    }
    
    companion object {
        const val LEFT_SIDE_PERCENT = 0.3
        const val DEFAULT_SHOW = true
    }
}

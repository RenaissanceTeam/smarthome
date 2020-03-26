package smarthome.client.presentation.scripts.setup.controllers

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import org.koin.core.inject
import smarthome.client.domain.api.conrollers.usecases.ObserveControllerUseCase
import smarthome.client.domain.api.devices.dto.GeneralDeviceInfo
import smarthome.client.domain.api.devices.usecase.GetGeneralDevicesInfo
import smarthome.client.domain.api.scripts.resolver.ControllerBlockResolver
import smarthome.client.domain.api.scripts.usecases.setup.GetBlockUseCase
import smarthome.client.entity.Controller
import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.controller.ControllerBlock
import smarthome.client.presentation.scripts.setup.controllers.epoxy.ControllerState
import smarthome.client.presentation.scripts.setup.controllers.epoxy.DeviceItemState
import smarthome.client.presentation.scripts.setup.graph.events.GraphEventBus
import smarthome.client.presentation.scripts.setup.graph.events.drag.BlockDragEvent
import smarthome.client.presentation.scripts.setup.graph.events.drag.CONTROLLERS_HUB
import smarthome.client.presentation.scripts.setup.graph.events.drag.DRAG_DROP
import smarthome.client.presentation.scripts.setup.graph.events.drag.DRAG_START
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.util.DataStatus
import smarthome.client.util.Position
import smarthome.client.util.data
import smarthome.client.util.runInScopeCatchingAny

class ControllersHubViewModel : KoinViewModel() {
    
    val devices = MutableLiveData<List<DeviceItemState>>()
    val controllers = mutableMapOf<Long, ControllerState>()
    
    private val observingController = mutableMapOf<Long, Boolean>()
    private val getGeneralDeviceInfo: GetGeneralDevicesInfo by inject()
    private val observeController: ObserveControllerUseCase by inject()
    private val controllerBlockResolver: ControllerBlockResolver by inject()
    private val graphEventBus: GraphEventBus by inject()
    private var width = 0f
    private val onFirstShow by lazy { fetchDevices() }
    
    init {
        disposable.add(graphEventBus.observe()
            .filter { it is BlockDragEvent && (it.isFromOrTo(CONTROLLERS_HUB)) }
            .map { it as BlockDragEvent }
            .subscribe { event ->
                when (event.status) {
                    DRAG_START -> if (event.isFrom(CONTROLLERS_HUB)) hideController(event.block)
                    DRAG_DROP -> if (event.isTo(CONTROLLERS_HUB)) handleDroppedController(event.block)
                }
            })
    }
    
    private fun handleDroppedController(block: Block) {
        if (block !is ControllerBlock) return
    
        val saved = controllers[block.controllerId] ?: return
        controllers[block.controllerId] = saved.copy(visible = true)
    
        startObservingController(block.controllerId)
        triggerDevicesRebuildModels()
    }
    
    fun onDropped(drag: BlockDragEvent) {
        graphEventBus.addEvent(drag.copy(to = CONTROLLERS_HUB, status = DRAG_DROP))
    }
    
    fun open() {
        onFirstShow
    }
    
    private fun hideController(block: Block) {
        if (block !is ControllerBlock) return
        
        val saved = controllers[block.controllerId] ?: return
        controllers[block.controllerId] = saved.copy(visible = false)
        
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
        disposable.add(observeController.execute(id).subscribe { newData ->
        
            val saved = controllers[id]
            controllers[id] = saved?.copy(
                controller = saved.controller,
                block = saved.block ?: createBlockIfDataReady(newData)
            ) ?: createNewControllerState(newData)
            
            triggerDevicesRebuildModels()
        })
    }
    
    private fun createNewControllerState(controller: DataStatus<Controller>): ControllerState {
        return ControllerState(controller, createBlockIfDataReady(controller))
    }
    
    private fun createBlockIfDataReady(controller: DataStatus<Controller>): ControllerBlock? {
        return controller.data?.let(controllerBlockResolver::resolve)
    }
    
    private fun deviceToDeviceItemState(device: GeneralDeviceInfo): DeviceItemState {
        return DeviceItemState(device.id, device.name, device.controllers)
    }
    
    fun onDragStarted(id: Long, dragTouch: Position): BlockDragEvent? {
        val controller = controllers[id]
            ?: throw IllegalStateException("Can't find dragged controller with id $id")
        
        if (controller.block == null) return null
        
        return BlockDragEvent(
            block = controller.block,
            status = DRAG_START,
            dragTouch = dragTouch,
            from = CONTROLLERS_HUB
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

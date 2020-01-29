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
import smarthome.client.presentation.scripts.addition.graph.CONTROLLERS_HUB
import smarthome.client.presentation.scripts.addition.graph.DRAG_START
import smarthome.client.presentation.scripts.addition.graph.events.drag.DragOperationInfo
import smarthome.client.presentation.scripts.addition.graph.Position
import smarthome.client.presentation.scripts.addition.graph.events.GraphEventBus
import smarthome.client.presentation.scripts.addition.graph.events.drag.ControllerDrag
import smarthome.client.presentation.scripts.addition.graph.events.drag.ControllerDragOperationInfo
import smarthome.client.presentation.scripts.addition.graph.events.drag.DragEvent
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.util.DataStatus

class ControllersViewViewModel : KoinViewModel() {
    
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
            .filter { it is DragEvent
                && it.info is ControllerDragOperationInfo
                && (it.info.from == CONTROLLERS_HUB || it.info.to == CONTROLLERS_HUB)
            }
            .subscribe {
                val info = (it as? DragEvent)?.info as? ControllerDragOperationInfo ?: return@subscribe
    
                when (it.info.status) {
                    DRAG_START -> hideController(info.id)
//                    is EndControllerDrag -> {
//                        // add this block to controllers (and start observing)
//                    }
                }
            })
    }
    
    fun onDropped(dragInfo: DragOperationInfo) {
    
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
//        return DragOperationInfo("controllersHub", dragTouch, "controller",
//            ControllerGraphBlockIdentifier(id)) { droppedTo ->
//            when (droppedTo) {
//                "controllersHub" -> {
//                    onDragCancelled(id)
//                    true
//                }
//                else -> false
//            }
//        }
        TODO()
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

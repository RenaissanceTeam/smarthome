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
import smarthome.client.presentation.scripts.addition.graph.*
import smarthome.client.presentation.scripts.addition.graph.events.GraphEventBus
import smarthome.client.presentation.scripts.addition.graph.events.drag.CommonDragInfo
import smarthome.client.presentation.scripts.addition.graph.events.drag.ControllerDragEvent
import smarthome.client.presentation.scripts.addition.graph.events.drag.GraphDragEvent
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
            .filter {
                it is ControllerDragEvent
                    && (it.dragInfo.from == CONTROLLERS_HUB || it.dragInfo.to == CONTROLLERS_HUB)
            }
            .subscribe {
                val info = (it as? ControllerDragEvent) ?: return@subscribe
    
                when (it.dragInfo.status) {
                    DRAG_START -> hideController(info.id)
//                    is EndControllerDrag -> {
//                        // add this block to controllers (and start observing)
//                    }
                }
            })
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
    
    fun onDragStarted(id: Long, dragTouch: Position): CommonDragInfo {
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

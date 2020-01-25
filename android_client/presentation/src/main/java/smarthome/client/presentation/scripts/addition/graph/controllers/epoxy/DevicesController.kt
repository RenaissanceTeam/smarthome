package smarthome.client.presentation.scripts.addition.graph.controllers.epoxy

import com.airbnb.epoxy.Typed2EpoxyController
import smarthome.client.presentation.components.emptyItemView
import smarthome.client.presentation.scripts.addition.graph.controllers.ControllersViewViewModel
import smarthome.client.util.data

class DevicesController : Typed2EpoxyController<List<DeviceItemState>, ControllersViewViewModel>() {
    
    override fun buildModels(devices: List<DeviceItemState>, viewModel: ControllersViewViewModel) {
        if (devices.isEmpty()) emptyItemView { id(0) }
        
        devices.forEach { device ->
            deviceView {
                id(device.id)
                
                deviceName(device.name)
                controllers(device.controllers.map { controllerId ->
                    val controller = viewModel.controllers[controllerId] ?: return@map null
                    if (!viewModel.shouldShow(controllerId)) return@map null
                    
                    ControllerViewModel_().apply {
                        id(controllerId)
                        name(controller.data?.name.orEmpty())
                        state(controller.data?.state.orEmpty())
                        
                        onDraggedToGraph { viewModel.onDraggedToGraph(controllerId) }
                    }
                }.filterNotNull())
            }
        }
    }
}
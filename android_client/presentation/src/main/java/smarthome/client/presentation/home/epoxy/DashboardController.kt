package smarthome.client.presentation.home.epoxy

import com.airbnb.epoxy.Typed2EpoxyController
import smarthome.client.domain.api.devices.dto.GeneralDeviceInfo
import smarthome.client.presentation.components.controllerView
import smarthome.client.presentation.components.deviceView
import smarthome.client.presentation.components.emptyItemView
import smarthome.client.presentation.home.DashboardViewModel
import smarthome.client.util.LoadingStatus
import smarthome.client.util.data

class DashboardController : Typed2EpoxyController<List<GeneralDeviceInfo>, DashboardViewModel>() {
    
    override fun buildModels(devices: List<GeneralDeviceInfo>, viewModel: DashboardViewModel) {
        if (devices.isEmpty() && viewModel.allHomeUpdateState.value == false) {
            emptyItemView { id(0) }
        }
        
        devices.forEach { device ->
            deviceView {
                id(device.id)
                name(device.name)
                type(device.type)
                
                onClick { viewModel.onDeviceClicked(device.id) }
            }
            device.controllers.forEach { id ->
                val controller = viewModel.controllers[id]
                controllerView {
                    id(id)
                    name(controller?.data?.name.orEmpty())
                    type(controller?.data?.type.orEmpty())
                    state(controller?.data?.state.orEmpty())
                    refreshing(controller is LoadingStatus)
                    
                    onClick { viewModel.onControllerClick(id) }
                    onLongClick { viewModel.onControllerLongClick(id) }
                }
            }
        }
        
    }
}
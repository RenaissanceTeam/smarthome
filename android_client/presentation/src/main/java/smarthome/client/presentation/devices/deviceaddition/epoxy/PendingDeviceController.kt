package smarthome.client.presentation.devices.deviceaddition.epoxy

import com.airbnb.epoxy.Typed2EpoxyController
import smarthome.client.presentation.components.emptyItemView
import smarthome.client.presentation.devices.deviceaddition.AdditionViewModel

class PendingDeviceController : Typed2EpoxyController<List<PendingDeviceItemState>, AdditionViewModel>() {
    
    override fun buildModels(devices: List<PendingDeviceItemState>, viewModel: AdditionViewModel) {
        if (devices.isEmpty() && viewModel.refresh.value == false) {
            emptyItemView { id(0) }
        }
        
        devices.forEach { state ->
            pendingDeviceView {
                id(state.device.id)
                device(state.device)
                expanded(state.isExpanded)
                acceptInProgress(state.acceptInProgress)
                declineInProgress(state.declineInProgress)
                
                onExpand { viewModel.onExpand(state.device.id) }
                onDeviceLongClicked { viewModel.onDeviceLongClicked(state.device.id) }
                onAccept { viewModel.acceptDevice(state.device.id) }
                onDecline { viewModel.declineDevice(state.device.id) }
                
                
                controllers(state.controllers.map { controller ->
                    
                    PendingControllerViewModel_().apply {
                        id(controller.id)
                        name(controller.name)
                        type(controller.type)
                        state(controller.state)
                        refreshing(controller.isRefreshing)
                        
                        spanSizeOverride { total, _, _ -> total/2 }
                        
                        onControllerClicked { viewModel.onControllerClicked(controller.id) }
                        onControllerLongClicked { viewModel.onControllerLongClicked(controller.id) }
                    }
                })
            }
        }
    }
}

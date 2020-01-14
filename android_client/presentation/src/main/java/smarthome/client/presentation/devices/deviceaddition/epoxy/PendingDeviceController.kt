package smarthome.client.presentation.devices.deviceaddition.epoxy

import com.airbnb.epoxy.Typed2EpoxyController
import smarthome.client.domain.api.devices.dto.GeneralDeviceInfo
import smarthome.client.presentation.devices.deviceaddition.AdditionViewModel

class PendingDeviceController : Typed2EpoxyController<List<PendingDeviceItemState>, AdditionViewModel>() {
    
    override fun buildModels(devices: List<PendingDeviceItemState>, viewModel: AdditionViewModel) {
        devices.forEach {
            pendingDeviceView {
                id(it.device.id)
                device(it.device)
                expanded(it.isExpanded)
                controllers(it.device.controllers.map { controller ->
                    PendingControllerViewModel_().apply {
                        id(controller.id)
                        name(controller.name)
                        type(controller.type)
                        state(controller.state)
                        refreshing(it.controllerRefreshing[controller.id] ?: false)
                        spanSizeOverride { total, _, _ -> total/2 }
                    }
                })
                onExpand { viewModel.onExpand(it.device.id) }
                onDeviceClick { viewModel.onDeviceClicked(it.device.id) }
            }
        }
    }
}

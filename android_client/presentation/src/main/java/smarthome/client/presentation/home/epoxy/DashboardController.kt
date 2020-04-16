package smarthome.client.presentation.home.epoxy

import com.airbnb.epoxy.Typed2EpoxyController
import smarthome.client.domain.api.devices.dto.GeneralDeviceInfo
import smarthome.client.presentation.components.controllerView
import smarthome.client.presentation.components.deviceView
import smarthome.client.presentation.components.dividerItem
import smarthome.client.presentation.components.emptyItemView
import smarthome.client.presentation.home.DashboardViewModel
import smarthome.client.util.LoadingStatus
import smarthome.client.util.data
import smarthome.client.util.forEachDivided

class DashboardController : Typed2EpoxyController<List<GeneralDeviceInfo>, DashboardViewModel>() {

    override fun buildModels(devices: List<GeneralDeviceInfo>, viewModel: DashboardViewModel) {
        if (devices.isEmpty() && viewModel.allHomeUpdateState.value == false) {
            emptyItemView { id(0) }
        }

        devices.forEachDivided(each = { device ->
            deviceView {
                id(device.id)
                name(device.name)
                type(device.type)

                onClick { viewModel.onDeviceClicked(device.id) }
            }
            device.controllers.forEachDivided({ id ->
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
            },
                    divide = { dividerItem { id(0) } }
            )
        },
                divide = { dividerItem { id(0) } }
        )
    }
}
package smarthome.client.presentation.devices.devicedetail.epoxy

import com.airbnb.epoxy.Typed2EpoxyController
import smarthome.client.entity.Controller
import smarthome.client.presentation.components.controllerView
import smarthome.client.presentation.components.emptyItemView
import smarthome.client.presentation.devices.devicedetail.DeviceDetailViewModel
import smarthome.client.util.DataStatus
import smarthome.client.util.LoadingStatus
import smarthome.client.util.data

class DeviceDetailsController :
    Typed2EpoxyController<List<DataStatus<Controller>>, DeviceDetailViewModel>() {
    override fun buildModels(controllers: List<DataStatus<Controller>>,
                             viewModel: DeviceDetailViewModel) {
        if (controllers.isEmpty() && viewModel.refresh.value == false) {
            emptyItemView {
                id(0)
            }
        }
        
        controllers.forEach {
            val id = it.data?.id ?: return@forEach
            
            controllerView {
                id(id)
                name(it.data?.name.orEmpty())
                state(it.data?.state.orEmpty())
                type(it.data?.type.orEmpty())
                refreshing(it is LoadingStatus)
    
                onClick { viewModel.onControllerClick(id) }
                onLongClick { viewModel.onControllerLongClick(id) }
            }
        }
    }
}
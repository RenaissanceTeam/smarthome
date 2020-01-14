package smarthome.client.presentation.devices.deviceaddition.epoxy

import smarthome.client.domain.api.devices.dto.GeneralDeviceInfo
import smarthome.client.entity.Controller
import smarthome.client.util.DataStatus

data class PendingDeviceItemState (
    val device: GeneralDeviceInfo,
    val controllers: List<PendingControllerItemState>,
    val isExpanded: Boolean,
    val acceptInProgress: Boolean,
    val declineInProgress: Boolean
)

data class PendingControllerItemState(
    val id: Long,
    val name: String,
    val type: String,
    val state: String,
    val isRefreshing: Boolean
)
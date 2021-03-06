package smarthome.client.presentation.devices.deviceaddition.epoxy

import smarthome.client.domain.api.devices.dto.GeneralDeviceInfo

data class PendingDeviceItemState (
    val device: GeneralDeviceInfo,
    val controllers: List<PendingControllerItemState>,
    val isExpanded: Boolean,
    val acceptInProgress: Boolean,
    val declineInProgress: Boolean
)


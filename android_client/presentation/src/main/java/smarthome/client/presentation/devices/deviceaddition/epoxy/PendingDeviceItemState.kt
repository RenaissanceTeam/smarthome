package smarthome.client.presentation.devices.deviceaddition.epoxy

import smarthome.client.domain.api.devices.dto.GeneralDeviceInfo

data class PendingDeviceItemState (
    val device: GeneralDeviceInfo,
    val isExpanded: Boolean,
    val controllerRefreshing: Map<Long, Boolean>
)
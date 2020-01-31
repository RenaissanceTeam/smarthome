package smarthome.client.presentation.devices.deviceaddition.epoxy

data class PendingControllerItemState(
    val id: Long,
    val name: String,
    val type: String,
    val state: String,
    val isRefreshing: Boolean
)
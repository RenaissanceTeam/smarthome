package smarthome.client.presentation.devices.deviceaddition.items

interface PendingDeviceActions {
    fun onDeviceClicked()
    fun onControllerClicked()
    fun onAccept()
    fun onReject()
}
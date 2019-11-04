package smarthome.library.common

open class IotDevice(var name: String = "",
                     var description: String? = null,
                     var serveState: DeviceServeState = DeviceServeState.IDLE,
                     val id: Id = Id(name),
                     var controllers: List<BaseController>) {

    override fun hashCode() = id.hashCode()

    override fun equals(other: Any?) = (other as? IotDevice)?.id == id
}

enum class DeviceServeState {
    DELETE, PENDING_TO_ADD, ACCEPT_PENDING_TO_ADD, IDLE
}
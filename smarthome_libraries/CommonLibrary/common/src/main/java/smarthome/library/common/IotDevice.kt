package smarthome.library.common

open class IotDevice(var name: String,
                     var description: String?,
                     var serveState: DeviceServeState = DeviceServeState.IDLE,
                     val guid: Long = GUID.getGuidForName(name),
                     var controllers: List<BaseController>) {

    override fun hashCode(): Int {
        return guid.toInt()
    }

    override fun equals(other: Any?): Boolean {
        return (other as? IotDevice)?.guid == guid
    }
}

enum class DeviceServeState {
    DELETE, PENDING_TO_ADD, ACCEPT_PENDING_TO_ADD, IDLE
}
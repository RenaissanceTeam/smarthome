package smarthome.library.common

open class IotDevice(var name: String,
                     var description: String?,
                     val guid: Long = GUID.getGuidForName(name),
                     var controllers: List<BaseController>) {

    override fun hashCode(): Int {
        return guid.toInt()
    }

    override fun equals(obj: Any?): Boolean {
        return (obj as? IotDevice)?.guid == guid
    }
}

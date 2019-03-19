package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight

import smarthome.library.common.GUID
import smarthome.library.common.IotDevice
import smarthome.library.common.constants.DeviceTypes.YEELIGHT_DEVICE_TYPE
import smarthome.raspberry.thirdpartydevices.network.SocketHolder
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.command.Command
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.constants.GET_PROPERTY_METHOD_HEADER
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.ControllersFactory
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.enums.Effect
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.enums.Property
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.model.DeviceMetaData
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.model.DiscoverResponse
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.result.Result
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.utils.Utils

class Device(private val id: String,
             val metaData: DeviceMetaData,
             val ip: String,
             val port: Int = 55443,
             var effect: Effect = Effect.SUDDEN,
             var duration: Int = 0)
    : IotDevice() {

    init {
        name = id
        guid = GUID.getInstance().getGuidForIotDevice(this)
        type = YEELIGHT_DEVICE_TYPE
    }

    constructor(discoverResponse: DiscoverResponse) : this(
            discoverResponse.id,
            discoverResponse.getMetaData(),
            discoverResponse.ip,
            discoverResponse.port) {

        for (service in metaData.support) {
            try {
                controllers.add(ControllersFactory.createController(this, service))
            } catch (e: IllegalArgumentException) { }
        }
    }

    private val socketHolder: SocketHolder = SocketHolder(ip, port)

    fun readUntilResult(id: Int): Result {
        while (true) {
            val data: String = socketHolder.read()
            val res: Result = Utils.mapJsonExpose(data, Result::class.java)

            if (res.isOkResult() && res.id == id)
                return res
            else if (res.id == id)
                res.getException()?.let { throw it }
        }
    }

    fun sendCommand(command: Command): Result {
        socketHolder.send(command.toJson() + "\r\n")
        return readUntilResult(command.id)
    }

    fun getProperty(property: Property): String {
        val request = Command(GET_PROPERTY_METHOD_HEADER, arrayOf(property.property))
        return sendCommand(request).result!![0]
    }

    fun getProperties(vararg properties: Property): Array<String> {
        val expectedProperties: Array<out Property> = if (properties.count() == 0) Property.values() else properties
        val expectedPropertiesValues: List<String> = expectedProperties.map { it.property }
        val request = Command(GET_PROPERTY_METHOD_HEADER, expectedPropertiesValues)
        return sendCommand(request).result!!
    }

    override fun equals(other: Any?): Boolean {
        val second = other as Device
        return second.id == this.id
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + ip.hashCode()
        return result
    }

    override fun toString(): String {
        return "\n--- Yeelight device ---\n" +
                "id: $id \n" +
                "ip: $ip \n" +
                "port: $port \n" +
                metaData.toString()
    }

}
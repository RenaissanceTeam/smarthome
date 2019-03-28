package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight

import com.google.firebase.firestore.Exclude
import com.google.gson.annotations.Expose
import smarthome.library.common.GUID
import smarthome.library.common.IotDevice
import smarthome.library.common.constants.DeviceTypes.YEELIGHT_DEVICE_TYPE
import smarthome.raspberry.thirdpartydevices.network.SocketHolder
import smarthome.raspberry.thirdpartydevices.utils.Utils
import smarthome.raspberry.thirdpartydevices.utils.Utils.Companion.toJson
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.command.Command
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.constants.GET_PROPERTY_METHOD_HEADER
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.ControllersFactory
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.enums.Effect
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.enums.Property
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.model.DeviceMetaData
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.model.DiscoverResponse
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.result.Result

class YeelightDevice(id: String,
                     metaData: DeviceMetaData,
                     ip: String,
                     port: Int = 55443,
                     effect: Effect = Effect.SMOOTH,
                     duration: Int = 500)
    : IotDevice() {

    @Expose private val id: String = id
    @Exclude @Expose val metaData: DeviceMetaData = metaData
        @Exclude get
    @Exclude @Expose val ip: String = ip
        @Exclude get
    @Exclude @Expose val port: Int = port
        @Exclude get
    @Exclude @Expose var effect: Effect = effect
        @Exclude get
    @Exclude @Expose var duration: Int = duration
        @Exclude get

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
        val second = other as IotDevice
        return second.guid == this.guid
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

    override fun gsonned(): String {
        return toJson(this)
    }

}
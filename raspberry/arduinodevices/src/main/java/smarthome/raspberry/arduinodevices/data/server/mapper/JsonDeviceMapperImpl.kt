package smarthome.raspberry.arduinodevices.data.server.mapper

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import smarthome.library.common.IotDevice
import smarthome.raspberry.arduinodevices.data.server.entity.InvalidDeviceException

class JsonDeviceMapperImpl(
    private val gson: Gson
) : JsonDeviceMapper {
    override fun map(device: String): IotDevice {
        try {
            return gson.fromJson<IotDevice>(device, IotDevice::class.java)
        } catch (e: JsonSyntaxException) {
            throw InvalidDeviceException(device, e)
        }
    }
}
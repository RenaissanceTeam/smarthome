package smarthome.raspberry.arduinodevices.data

import org.springframework.stereotype.Component
import org.springframework.web.client.RestOperations

@Component
class ArduinoDeviceApiFactory(
        private val rest: RestOperations
) {
    fun getForAddress(address: String): ArduinoDeviceApi {
        return ArduinoDeviceApiImpl(address, rest)
    }
}


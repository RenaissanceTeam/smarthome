package smarthome.raspberry.arduinodevices.data

import org.springframework.web.client.RestOperations
import org.springframework.web.client.getForObject
import org.springframework.web.client.postForObject

class ArduinoDeviceApiImpl(private val address: String, private val rest: RestOperations) : ArduinoDeviceApi {

    override fun readController(controllerIndex: Int): String {
        return rest.getForObject("http://$address/controller/$controllerIndex")
    }

    override fun writeStateToController(controllerIndex: Int, value: String): String {
        return rest.postForObject("http://$address/controller/$controllerIndex", value)
    }
}
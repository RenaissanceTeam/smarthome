package smarthome.raspberry.arduinodevices.data

import org.springframework.web.client.RestOperations
import org.springframework.web.client.getForObject
import org.springframework.web.client.postForObject

class ArduinoDeviceApiImpl(private val ip: String, private val rest: RestOperations) : ArduinoDeviceApi {

    override fun readController(controllerIndex: Int): String {
        return rest.getForObject("http://$ip/controller/$controllerIndex")
    }

    override fun writeStateToController(controllerIndex: Int, value: String): String {
        return rest.postForObject("http://$ip/controller/$controllerIndex", value)
    }
}
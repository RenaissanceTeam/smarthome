package smarthome.raspberry.arduinodevices.controllers.data.api

import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestOperations
import smarthome.raspberry.arduinodevices.controllers.data.dto.ArduinoControllerResponse
import smarthome.raspberry.arduinodevices.channel.domain.exceptions.ArduinoChannelException

class ArduinoDeviceApiImpl(address: String, port: String, private val rest: RestOperations) : ArduinoDeviceApi {
    private val url = "http://$address:$port"

    override fun readController(controllerIndex: Int): String {
        return rest.runCatching {
            getForEntity(
                    "$url/service?i=$controllerIndex",
                    ArduinoControllerResponse::class.java
            ).let { takeBodyOrThrow(it).response }
        }
                .onFailure { throw ArduinoChannelException(it) }
                .getOrThrow()
    }

    private fun takeBodyOrThrow(it: ResponseEntity<ArduinoControllerResponse>) =
            it.body ?: throw ArduinoChannelException(RuntimeException("Null body response"))

    override fun writeStateToController(controllerIndex: Int, value: String): String {
        return rest.runCatching {
            postForEntity(
                    "$url/service?i=$controllerIndex&v=$value",
                    null,
                    ArduinoControllerResponse::class.java
            ).let { takeBodyOrThrow(it).response }
        }
                .onFailure { throw ArduinoChannelException(it) }
                .getOrThrow()
    }
}
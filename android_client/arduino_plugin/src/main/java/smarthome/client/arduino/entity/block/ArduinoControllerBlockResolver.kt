package smarthome.client.arduino.entity.block

import smarthome.client.arduino.entity.dht
import smarthome.client.arduino.entity.onoff
import smarthome.client.domain.api.scripts.resolver.ControllerBlockResolver
import smarthome.client.entity.Controller
import smarthome.client.entity.script.controller.ControllerBlock
import java.util.*

class ArduinoControllerBlockResolver : ControllerBlockResolver {
    
    override fun canResolve(item: Controller): Boolean {
        return possibleTypes.contains(item.type)
    }
    
    override fun resolve(item: Controller): ControllerBlock {
        return ArduinoControllerBlock(
            id = UUID.randomUUID().toString(),
            controllerId = item.id,
            type = item.type
        )
    }
    
    companion object {
        private val possibleTypes = listOf(dht, onoff)
    }
}
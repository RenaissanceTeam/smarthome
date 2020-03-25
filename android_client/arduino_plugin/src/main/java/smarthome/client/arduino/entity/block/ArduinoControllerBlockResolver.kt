package smarthome.client.arduino.entity.block

import smarthome.client.arduino.entity.dht
import smarthome.client.arduino.entity.onoff
import smarthome.client.domain.api.scripts.resolver.ControllerBlockResolver
import smarthome.client.entity.Controller
import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.controller.ControllerBlockId
import smarthome.client.util.Position

class ArduinoControllerBlockResolver: ControllerBlockResolver {
    
    override fun canResolve(controller: Controller): Boolean {
        return possibleTypes.contains(controller.type)
    }
    
    override fun resolve(controller: Controller, position: Position): Block? {
        return ArduinoControllerBlock(
            id = ControllerBlockId(),
            controllerId = controller.id,
            type = controller.type,
            position = position
        )
    }
    
    companion object {
        private val possibleTypes = listOf(dht, onoff)
    }
}
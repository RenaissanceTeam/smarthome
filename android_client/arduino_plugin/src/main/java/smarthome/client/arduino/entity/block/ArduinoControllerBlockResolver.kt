package smarthome.client.arduino.entity.block

import smarthome.client.domain.api.scripts.resolver.ControllerBlockResolver
import smarthome.client.entity.Controller
import smarthome.client.entity.script.block.Block
import smarthome.client.util.Position

class ArduinoControllerBlockResolver: ControllerBlockResolver {
    
    override fun canResolve(controller: Controller): Boolean {
        return possibleTypes.contains(controller.type)
    }
    
    override fun resolve(controller: Controller, position: Position): Block? {
        return ControllerBlock(controller.id, controller.type, position)
    }
    
    companion object {
        private const val dht = "dht"
        private const val onoff = "onoff"
        private val possibleTypes = listOf(dht, onoff)
    }
}
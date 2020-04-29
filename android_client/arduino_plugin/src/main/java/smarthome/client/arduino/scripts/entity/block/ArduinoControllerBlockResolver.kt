package smarthome.client.arduino.scripts.entity.block

import smarthome.client.arduino.scripts.entity.*
import smarthome.client.domain.api.scripts.resolver.ControllerBlockResolver
import smarthome.client.entity.Controller
import smarthome.client.entity.script.controller.ControllerBlock
import smarthome.client.util.generateId

class ArduinoControllerBlockResolver : ControllerBlockResolver {

    override fun canResolve(item: Controller): Boolean {
        return possibleTypes.contains(item.type)
    }

    override fun resolve(item: Controller): ControllerBlock {
        return ArduinoControllerBlock(
                id = generateId(),
                controllerId = item.id,
                type = item.type
        )
    }

    companion object {
        private val possibleTypes = listOf(
                analog,
                onoff,
                temperature11,
                humidity11,
                digital,
                temperature22,
                humidity22,
                pressure)
    }
}
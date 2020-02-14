package smarthome.client.domain.api.scripts.resolver

import smarthome.client.entity.Controller
import smarthome.client.entity.script.block.Block
import smarthome.client.util.Position

interface ControllerBlockResolver {
    fun canResolve(controller: Controller): Boolean
    fun resolve(controller: Controller, position: Position): Block?
}
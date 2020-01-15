package smarthome.client.presentation.controllers.controllerdetail.statechanger

import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import smarthome.client.entity.Controller

class StateChangerFactory : KoinComponent {
    fun get(controller: Controller): ControllerStateChanger = runCatching {
        get<ControllerStateChanger>(named(controller.type), parameters = { parametersOf(controller.id) })
    }.getOrElse { throw NoStateChanger(controller.type) }
}


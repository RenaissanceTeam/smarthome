package smarthome.raspberry.arduinodevices.data.server.httphandlers

import smarthome.library.common.Id
import smarthome.raspberry.arduinodevices.data.server.api.RequestHandler
import smarthome.raspberry.arduinodevices.data.server.entity.*
import smarthome.raspberry.arduinodevices.data.mapper.ValuePayloadToControllerStateMapper
import smarthome.raspberry.arduinodevices.data.server.takeIfNotEmpty
import smarthome.raspberry.controllers.api.domain.GetControllerByIdUseCase
import smarthome.raspberry.controllers.api.domain.OnControllerChangedWithoutUserRequestUseCase

class AlertPost(
    private val getControllerByIdUseCase: GetControllerByIdUseCase,
    private val onControllerChangedWithoutUserRequestUseCase: OnControllerChangedWithoutUserRequestUseCase,
    private val valueMapper: ValuePayloadToControllerStateMapper
    
) : RequestHandler {
    override val identifier = RequestIdentifier(
        Method.POST,
        "/iot/alert",
        parameters = setOf(
            controllerId, value
        )
    )
    
    override suspend fun serve(request: Request): Response {
        return withCaughtErrors {
            val id = request.params.takeIfNotEmpty(controllerId)
            val value = request.params.takeIfNotEmpty(value)
        
            val controller = getControllerByIdUseCase.execute(Id(id))
            val newState = valueMapper.map(value)
        
            onControllerChangedWithoutUserRequestUseCase.execute(controller, newState)
            success()
        }
    }
}

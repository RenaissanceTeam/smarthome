package smarthome.raspberry.arduinodevices.data.server.httphandlers

import smarthome.library.common.Id
import smarthome.raspberry.arduinodevices.data.server.api.RequestHandler
import smarthome.raspberry.arduinodevices.data.server.entity.*
import smarthome.raspberry.arduinodevices.data.server.mapper.ValuePayloadToControllerStateMapper
import smarthome.raspberry.controllers.api.domain.GetControllerByIdUseCase
import smarthome.raspberry.controllers.api.domain.OnControllerChangedWithoutUserRequestUseCase

class AlertPost(
    private val getControllerByIdUseCase: GetControllerByIdUseCase,
    private val onControllerChangedWithoutUserRequestUseCase: OnControllerChangedWithoutUserRequestUseCase,
    private val valueMapper: ValuePayloadToControllerStateMapper
) : RequestHandler {
    override val identifier = RequestIdentifier(Method.POST, "/iot/alert", parameters = setOf(
        controllerId, value
    ))
    
    override suspend fun serve(parameters: Map<String, String>): Response {
        return try {
            val id = parameters[controllerId]?.takeIf { it.isNotEmpty() } ?: throw BadParams()
            val value = parameters[value] ?: throw BadParams()
            
            val controller = getControllerByIdUseCase.execute(Id(id))
            val newState = valueMapper.map(value)
                
            onControllerChangedWithoutUserRequestUseCase.execute(controller, newState)
            
            success()
        } catch (e: NumberFormatException) {
            badRequest("passed bad controller id. params=$parameters")
        } catch (e: BadParams) {
            badRequest("bad params=$parameters")
        }
        
        
    }
}

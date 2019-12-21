package smarthome.raspberry.arduinodevices.data.server.httphandlers

import smarthome.raspberry.arduinodevices.data.server.api.RequestHandler
import smarthome.raspberry.arduinodevices.data.server.entity.*
import smarthome.raspberry.arduinodevices.data.server.mapper.JsonDeviceMapper
import smarthome.raspberry.devices.api.domain.AddDeviceUseCase

class InitPost(
    private val deviceMapper: JsonDeviceMapper,
    private val addDeviceUseCase: AddDeviceUseCase
) : RequestHandler {
    override val identifier = RequestIdentifier(
        Method.POST,
        "/iot/init",
        parameters = setOf(
            body
        )
    )
    
    override suspend fun serve(request: Request): Response {
        return withCaughtErrors {
            val deviceJson = request.body.takeIf { it.isNotEmpty() } ?: throw BadParams("empty body for init post")
            val device = deviceMapper.map(deviceJson)
            
            addDeviceUseCase.execute(device)
            
            success()
        }
    }
}

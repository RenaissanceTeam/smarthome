package smarthome.raspberry.arduinodevices.data.server.httphandlers

import smarthome.raspberry.arduinodevices.data.server.entity.BadParams
import smarthome.raspberry.arduinodevices.data.server.entity.Response
import smarthome.raspberry.arduinodevices.data.server.entity.badRequest

suspend fun withCaughtErrors(block: suspend () -> Response): Response {
    return try {
        block()
    } catch (e: BadParams) {
        badRequest(e.info)
    } catch (e: Throwable) {
        badRequest(e.message.orEmpty())
    }
}
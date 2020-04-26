package smarthome.raspberry.arduinodevices.controllers.data

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import smarthome.raspberry.arduinodevices.channel.domain.exceptions.ArduinoChannelException
import smarthome.raspberry.util.toResponseEntityStatus

@ControllerAdvice
class ArduinoExceptionsMapper {
    @ExceptionHandler
    fun channelException(e: ArduinoChannelException) = e.toResponseEntityStatus(HttpStatus.BAD_REQUEST, message = e.toString())
}
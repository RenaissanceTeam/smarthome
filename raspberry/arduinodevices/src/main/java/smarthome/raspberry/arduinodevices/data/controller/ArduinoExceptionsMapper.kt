package smarthome.raspberry.arduinodevices.data.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import smarthome.raspberry.arduinodevices.domain.exceptions.ArduinoChannelException
import smarthome.raspberry.util.toResponseEntityStatus

@ControllerAdvice
class ArduinoExceptionsMapper {
    @ExceptionHandler
    fun channelException(e: ArduinoChannelException) = e.toResponseEntityStatus(HttpStatus.BAD_REQUEST)
}
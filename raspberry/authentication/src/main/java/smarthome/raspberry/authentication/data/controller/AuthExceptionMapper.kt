package smarthome.raspberry.authentication.data.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import smarthome.raspberry.authentication.api.domain.exceptions.IllegalRegistrationCode
import smarthome.raspberry.authentication.api.domain.exceptions.NoAuthenticatedUserException
import smarthome.raspberry.authentication.api.domain.exceptions.NotSignedInException
import smarthome.raspberry.authentication.api.domain.exceptions.UserExistsException
import smarthome.raspberry.util.toResponseEntityStatus

@ControllerAdvice
class AuthExceptionMapper {

    @ExceptionHandler
    fun userExists(e: UserExistsException) = e.toResponseEntityStatus(HttpStatus.BAD_REQUEST, "User already exists")

    @ExceptionHandler
    fun notSignedIn(e: NotSignedInException) = e.toResponseEntityStatus(HttpStatus.UNAUTHORIZED, "Not signed in")

    @ExceptionHandler
    fun noAuthenticatedUser(e: NoAuthenticatedUserException) = e.toResponseEntityStatus(HttpStatus.UNAUTHORIZED,
            "Can't find authenticated user")

    @ExceptionHandler
    fun illegalRegistrationCode(e: IllegalRegistrationCode) = e.toResponseEntityStatus(HttpStatus.BAD_REQUEST,
            "Registration code does not match")
}
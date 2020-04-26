package smarthome.raspberry.util

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

fun Throwable.toResponseEntityStatus(status: HttpStatus, message: String? = null) = ResponseEntity(message
        ?: this.message.orEmpty(), status)

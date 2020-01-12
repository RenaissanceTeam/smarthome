package smarthome.raspberry.util.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

val notFound = ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource")
package smarthome.raspberry.arduinodevices.data.server.entity

data class Response(val code: Int,
                    val mimeType: String,
                    val payload: String)

val notFound = Response(NOT_FOUND_CODE, plainText, "Resource not found")
fun badRequest(message: String = "") = Response(BAD_REQUEST_CODE, plainText, message)
fun success(message: String = "") = Response(SUCCESS_CODE, plainText, message)
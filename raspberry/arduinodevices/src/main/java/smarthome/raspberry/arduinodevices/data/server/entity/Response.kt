package smarthome.raspberry.arduinodevices.data.server.entity

data class Response(val code: Int,
                    val mimeType: String,
                    val payload: String)

val notFound = Response(NOT_FOUND_CODE, plainText, "Resource not found")

package smarthome.raspberry.arduinodevices.server

import android.util.Log
import fi.iki.elonen.NanoHTTPD
import kotlinx.coroutines.runBlocking
import java.io.IOException

const val TAG = "WebServer"
const val PORT = 8080

class WebServer : NanoHTTPD(PORT), StoppableServer {
    override fun serve(session: IHTTPSession): Response {
        return try {
            val response = runBlocking { HandlerType.handle(session) }
            response
        } catch (e: Exception) {
            Log.d(TAG, "can't serve: $e")
            HandlerType.errorHandle("No suitable method found")
        }

    }

    override fun startServer() {
        try {
            start()
        } catch (e: IOException) {
            Log.e(TAG, "startServer: ", e)
        }

    }

    override fun stopServer() {
        stop()
    }
}

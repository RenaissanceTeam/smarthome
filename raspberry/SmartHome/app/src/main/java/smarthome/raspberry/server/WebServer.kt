package smarthome.raspberry.server

import android.util.Log

import java.io.IOException

import fi.iki.elonen.NanoHTTPD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

const val TAG = "WebServer"
const val PORT = 8080

class WebServer : NanoHTTPD(PORT), StoppableServer {
    override fun serve(session: NanoHTTPD.IHTTPSession): NanoHTTPD.Response {
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

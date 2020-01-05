package smarthome.raspberry.arduinodevices.data.server.api


interface WebServer {
    fun setHandler(handler: RequestHandler)
    fun start()
    fun stop()
}

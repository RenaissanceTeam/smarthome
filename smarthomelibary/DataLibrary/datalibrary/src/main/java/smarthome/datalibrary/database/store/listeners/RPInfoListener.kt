package smarthome.datalibrary.database.store.listeners

interface RPInfoListener {
    fun onRaspberryIpReceived(ip: String)
    fun onRaspberryPortReceived(port: String)
}

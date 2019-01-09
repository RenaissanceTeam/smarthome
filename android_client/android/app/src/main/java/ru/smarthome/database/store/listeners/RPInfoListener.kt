package ru.smarthome.database.store.listeners

interface RPInfoListener {
    fun onRaspberryIpReceived(ip: String)
}

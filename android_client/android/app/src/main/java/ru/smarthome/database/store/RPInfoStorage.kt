package ru.smarthome.database.store

import com.google.firebase.database.DatabaseReference

import ru.smarthome.database.store.listeners.RPInfoListener

interface RPInfoStorage {
    fun postRaspberryIp(ip: String)
    fun postRaspberryIp(ip: String, listener: DatabaseReference.CompletionListener)
    fun getRaspberryInfo(listener: RPInfoListener)
}

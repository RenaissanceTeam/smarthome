package smarthome.datalibrary.database.store

import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseReference

import smarthome.datalibrary.database.store.listeners.RPInfoListener

interface RPInfoStorage {
    fun postRaspberryIp(ip: String)
    fun postRaspberryIp(ip: String, listener: OnSuccessListener<Void>)
    fun postRaspberryPort(port: String)
    fun postRaspberryPort(port: String, listener: OnSuccessListener<Void>)
    fun getRaspberryInfo(listener: RPInfoListener)
}

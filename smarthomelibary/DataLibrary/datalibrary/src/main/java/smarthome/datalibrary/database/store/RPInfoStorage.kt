package smarthome.datalibrary.database.store

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import smarthome.datalibrary.database.constants.Constants.defFailureListener
import smarthome.datalibrary.database.constants.Constants.defSuccessListener
import smarthome.datalibrary.database.store.listeners.RPInfoListener

interface RPInfoStorage {
    fun postRaspberryIp(ip: String, successListener: OnSuccessListener<Void> = defSuccessListener, failureListener: OnFailureListener = defFailureListener)
    fun postRaspberryPort(port: String, successListener: OnSuccessListener<Void> = defSuccessListener, failureListener: OnFailureListener = defFailureListener)
    fun getRaspberryInfo(listener: RPInfoListener)
}


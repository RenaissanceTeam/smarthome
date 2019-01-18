package smarthome.datalibrary.database.store

import com.google.android.gms.tasks.OnSuccessListener
import ru.smarthome.library.SmartHome
import smarthome.datalibrary.database.store.listeners.SmartHomeListener

interface SmartHomeStorage {
    fun postSmartHome(smartHome: SmartHome)
    fun postSmartHome(smartHome: SmartHome, listener: OnSuccessListener<Void>)
    fun getSmartHome(listener: SmartHomeListener)
}
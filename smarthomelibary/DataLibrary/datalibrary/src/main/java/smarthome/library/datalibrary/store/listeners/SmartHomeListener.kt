package smarthome.library.datalibrary.store.listeners

import ru.smarthome.library.SmartHome

interface SmartHomeListener {
    fun onSmartHomeReceived(smartHome: SmartHome)
}
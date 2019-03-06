package smarthome.library.datalibrary.store.listeners

import smarthome.library.common.SmartHome

interface SmartHomeListener {
    fun onSmartHomeReceived(smartHome: SmartHome)
}
package ru.smarthome.database.store.listeners

import ru.smarthome.library.SmartHome

interface SmartHomeListener {
    fun onSmartHomeReceived(smartHome: SmartHome)
}
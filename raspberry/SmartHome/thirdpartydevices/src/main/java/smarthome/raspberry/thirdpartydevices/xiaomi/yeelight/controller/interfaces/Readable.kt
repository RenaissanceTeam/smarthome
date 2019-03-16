package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces

import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.enums.Property

interface Readable {
    fun read(property: Property): String
}
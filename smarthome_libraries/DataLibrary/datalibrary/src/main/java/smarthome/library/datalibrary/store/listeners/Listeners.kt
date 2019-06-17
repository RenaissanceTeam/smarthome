package smarthome.library.datalibrary.store.listeners

import smarthome.library.common.IotDevice
import smarthome.library.common.SmartHome
import smarthome.library.datalibrary.model.HomesReferences

class DeviceUpdate(val devices: MutableList<IotDevice>, val isInnerCall: Boolean)

typealias MessageListener = (messages: List<Any>, isInnerCall: Boolean) -> Unit

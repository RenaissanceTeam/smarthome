package smarthome.library.common

class DeviceUpdate(val devices: MutableList<IotDevice>, val isInnerCall: Boolean)

typealias MessageListener = (messages: List<Any>, isInnerCall: Boolean) -> Unit

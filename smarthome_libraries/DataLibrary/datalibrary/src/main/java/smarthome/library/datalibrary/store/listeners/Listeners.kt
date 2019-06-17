package smarthome.library.datalibrary.store.listeners

import smarthome.library.common.IotDevice
import smarthome.library.common.SmartHome
import smarthome.library.datalibrary.model.HomesReferences

typealias DevicesObserver = (List<IotDevice>, isLocal: Boolean) -> Unit
typealias DeviceListener = (IotDevice) -> Unit
typealias OnHomeExists = (Boolean) -> Unit
typealias HomesReferencesListener = (HomesReferences) -> Unit
typealias MessageListener = (messages: List<Any>, isInnerCall: Boolean) -> Unit
typealias PendingDevicesFetchListener = (pendingDevices: List<IotDevice>) -> Unit
typealias SmartHomeListener = (SmartHome) -> Unit


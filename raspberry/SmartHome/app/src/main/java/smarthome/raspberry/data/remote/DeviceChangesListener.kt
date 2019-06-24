package smarthome.raspberry.data.remote

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import smarthome.library.common.IotDevice
import smarthome.library.datalibrary.store.SmartHomeStorage
import smarthome.raspberry.OddDeviceInCloud

object DeviceChangesListener {

    private val ioScope = CoroutineScope(Dispatchers.IO)


}
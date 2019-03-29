package smarthome.library.datalibrary.store

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice
import smarthome.library.common.SmartHome
import smarthome.library.datalibrary.constants.defFailureListener
import smarthome.library.datalibrary.constants.defSuccessListener
import smarthome.library.datalibrary.store.listeners.DeviceListener
import smarthome.library.datalibrary.store.listeners.DevicesObserver
import smarthome.library.datalibrary.store.listeners.SmartHomeListener

interface SmartHomeStorage {

    fun createSmartHome(
        successListener: OnSuccessListener<Void> = defSuccessListener,
        failureListener: OnFailureListener = defFailureListener
    )

    fun postSmartHome(
        smartHome: SmartHome,
        successListener: OnSuccessListener<Void> = defSuccessListener,
        failureListener: OnFailureListener = defFailureListener
    )

    fun getSmartHome(listener: SmartHomeListener, failureListener: OnFailureListener = defFailureListener)

    fun addDevice(
        iotDevice: IotDevice,
        successListener: OnSuccessListener<Void> = defSuccessListener,
        failureListener: OnFailureListener = defFailureListener
    )

    fun updateDevice(
        device: IotDevice,
        successListener: OnSuccessListener<Void> = defSuccessListener,
        failureListener: OnFailureListener = defFailureListener
    )

    fun getDevice(
        guid: Long,
        listener: DeviceListener,
        failureListener: OnFailureListener = defFailureListener
    )

    fun removeDevice(
        iotDevice: IotDevice,
        successListener: OnSuccessListener<Void> = defSuccessListener,
        failureListener: OnFailureListener = defFailureListener
    )

    fun observeDevicesUpdates(
        observer: DevicesObserver
    )

    fun detachDevicesUpdatesObserver()

    fun addPendingDevice(
        iotDevice: IotDevice,
        successListener: OnSuccessListener<Void> = defSuccessListener,
        failureListener: OnFailureListener = defFailureListener
    )

    fun removePendingDevice(
        iotDevice: IotDevice,
        successListener: OnSuccessListener<Void> = defSuccessListener,
        failureListener: OnFailureListener = defFailureListener
    )

}
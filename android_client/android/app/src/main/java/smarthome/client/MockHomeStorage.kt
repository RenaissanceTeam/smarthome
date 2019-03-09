package smarthome.client

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import retrofit2.Response
import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice
import smarthome.library.common.RaspberryResponse
import smarthome.library.common.SmartHome
import smarthome.library.datalibrary.store.SmartHomeStorage
import smarthome.library.datalibrary.store.listeners.DeviceListener
import smarthome.library.datalibrary.store.listeners.DevicesObserver
import smarthome.library.datalibrary.store.listeners.SmartHomeListener

class MockHomeStorage : SmartHomeStorage {

    private var smartHomeState: SmartHome? = null

    private val TAG = "MockHomeStorage"

    override fun addDevice(iotDevice: IotDevice, successListener: OnSuccessListener<Void>, failureListener: OnFailureListener) {

    }

    override fun detachDevicesUpdatesObserver() {

    }

    override fun getDevice(guid: Long, listener: DeviceListener, failureListener: OnFailureListener) {
        smartHomeState?.devices?.find { it.guid == guid }
                ?.let { listener.onDeviceReceived(it) }
                ?: failureListener.onFailure(RuntimeException("no such device $guid"))
    }

    override fun getSmartHome(listener: SmartHomeListener) {
        smartHomeState?.let { listener.onSmartHomeReceived(it) }
                ?: requestHomeStateFromRaspberry(listener)
    }

    private fun requestHomeStateFromRaspberry(listener: SmartHomeListener) {
        if (BuildConfig.DEBUG) Log.d(TAG, "make http request to raspberry to get state")

        Model.raspberryApi.getSmartHomeState().enqueue {
            onResponse = response@{ response ->
                if (BuildConfig.DEBUG) Log.d(TAG, "response: $response")
                if (response.isSuccessful) {
                    if (BuildConfig.DEBUG) Log.d(TAG, "responseBody= ${response.body()}")

                    val newSmartHome = response.body() ?: return@response // todo notify about error
                    listener.onSmartHomeReceived(newSmartHome)
                }
                // todo notify about error
            }

            onFailure = {
                // todo notify about error
            }
        }
    }

    override fun observeDevicesUpdates(observer: DevicesObserver) {

    }

    override fun postSmartHome(smartHome: SmartHome, successListener: OnSuccessListener<Void>, failureListener: OnFailureListener) {

    }

    override fun removeDevice(iotDevice: IotDevice, successListener: OnSuccessListener<Void>, failureListener: OnFailureListener) {

    }

    override fun updateDevice(device: IotDevice, controller: BaseController?, successListener: OnSuccessListener<Void>, failureListener: OnFailureListener) {
        if (BuildConfig.DEBUG) Log.d(TAG, "read $controller")
        controller ?: return

        val controllerGuid = controller.guid

        Model.raspberryApi.readControllerState(controllerGuid).enqueue {
            onResponse = { response ->
                if (response.isSuccessful) {
                    smartHomeState?.devices?.find { it == device }?.controllers?.find { it == controller }?.state = response.body()?.response
                    successListener.onSuccess(null)
                } else {
                    val message = "Returned code: ${response.code()}, body=${response.raw().body()?.string()}"
                }
            }

            onFailure = { failureListener.onFailure(it as Exception) }
        }
    }
}
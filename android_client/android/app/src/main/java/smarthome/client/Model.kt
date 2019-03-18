package smarthome.client

import android.util.Log
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import io.reactivex.Observable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import smarthome.client.BuildConfig.DEBUG
import smarthome.client.util.CloudStorages
import smarthome.client.util.FcmTokenStorage
import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice
import smarthome.library.common.SmartHome
import smarthome.library.datalibrary.store.listeners.DevicesObserver
import kotlin.coroutines.suspendCoroutine

// todo should not be a singleton
object Model {
    private val TAG = Model::class.java.simpleName
    private var smartHome: SmartHome? = null
    private var _devices: Observable<MutableList<IotDevice>>? = null
    private val ioScope = CoroutineScope(Dispatchers.IO)

    suspend fun getDevicesObservable(): Observable<MutableList<IotDevice>> = _devices
            ?: createDevicesObservable()

    private suspend fun createDevicesObservable(): Observable<MutableList<IotDevice>> {
        val storage = CloudStorages.getSmartHomeStorage()
        val observable = Observable.create<MutableList<IotDevice>> { emitter ->
            storage.observeDevicesUpdates(DevicesObserver { devices, isInner ->
                val newHome = SmartHome()
                newHome.devices = devices
                smartHome = newHome
                emitter.onNext(devices)
            }
            )
        }
        _devices = observable
        return observable
    }

    suspend fun getController(guid: Long): BaseController {
        return getController(getDevices(), guid)
    }

    fun getController(devices: List<IotDevice>, guid: Long): BaseController {
        var controller: BaseController? = null
        devices.find {
            controller = it.controllers.find { it.guid == guid }
            controller != null
        }

        return controller ?: throw NoControllerException(guid)
    }

    suspend fun getDevice(guid: Long): IotDevice {
        return getDevice(getDevices(), guid)
    }

    suspend fun getDevice(controller: BaseController): IotDevice {
        return getDevice(getDevices(), controller)
    }

    fun getDevice(devices: List<IotDevice>, controller: BaseController): IotDevice {
        return devices.find { it.controllers.contains(controller) }
                ?: throw NoDeviceWithControllerException(controller)
    }

    fun getDevice(devices: List<IotDevice>, deviceGuid: Long): IotDevice {
        return devices.find { it.guid == deviceGuid } ?: throw NoDeviceException(deviceGuid)
    }

    suspend fun getDevices(): MutableList<IotDevice> {
        val home = getSmartHome()
        return home.devices
    }


    suspend fun changeDevice(device: IotDevice) {
        val storage = CloudStorages.getSmartHomeStorage()
        suspendCoroutine<Unit> { continuation ->
            storage.updateDevice(device,
                    OnSuccessListener { continuation.resumeWith(Result.success(Unit)) },
                    OnFailureListener { continuation.resumeWith(Result.failure(RemoteFailure(it))) }
            )
        }
    }

    private suspend fun getSmartHome() = smartHome ?: loadHome()

    private suspend fun loadHome(): SmartHome {
        val smartHome = CloudStorages.loadHome()
        this.smartHome = smartHome
        return smartHome
    }

    fun saveInstanceToken(token: String, fcmTokenStorage: FcmTokenStorage) {
        ioScope.launch {
            try {
                CloudStorages.saveInstanceToken(token)
            } catch (e: Throwable) {
                if (DEBUG) Log.w(TAG, "failed to save instance token", e)
                fcmTokenStorage.removeToken()
            }
        }
    }

}
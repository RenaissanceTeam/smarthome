package smarthome.client

import android.util.Log
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import io.reactivex.Observable
import io.reactivex.subjects.ReplaySubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import smarthome.client.BuildConfig.DEBUG
import smarthome.library.common.scripts.Script
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
    private var pendingDevices: MutableList<IotDevice>? = null
    private var _devices: Observable<MutableList<IotDevice>>? = null
    private var pendingDevicesObservable: Observable<MutableList<IotDevice>>? = null
    private val ioScope = CoroutineScope(Dispatchers.IO)
    private var scripts = mutableListOf<Script>()
    private var _scriptsObservable: ReplaySubject<MutableList<Script>>? = null

    suspend fun getScriptsObservable(): Observable<MutableList<Script>> = _scriptsObservable ?: createScriptsObservable()

    private suspend fun createScriptsObservable(): Observable<MutableList<Script>> {
        val observable: ReplaySubject<MutableList<Script>> = ReplaySubject.createWithSize(1)
        _scriptsObservable = observable
        return observable
    }

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

    suspend fun getPendingDevicesObservable(): Observable<MutableList<IotDevice>> = pendingDevicesObservable
            ?: createPendingDevicesObservable()

    private suspend fun createPendingDevicesObservable(): Observable<MutableList<IotDevice>> {
        val storage = CloudStorages.getSmartHomeStorage()
        val observable = Observable.create<MutableList<IotDevice>> { emitter ->
            storage.observePendingDevicesUpdates(DevicesObserver { devices, isInner ->
                pendingDevices = devices
                emitter.onNext(devices)
            }
            )
        }
        pendingDevicesObservable = observable
        return observable
    }

    suspend fun getPendingController(guid: Long): BaseController {
        return getController(getPendingDevices(), guid)
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

    suspend fun getPendingDevice(guid: Long): IotDevice {
        return getDevice(getPendingDevices(), guid)
    }

    suspend fun getDevice(controller: BaseController): IotDevice {
        return getDevice(getDevices(), controller)
    }

    suspend fun getPendingDevice(controller: BaseController): IotDevice {
        return getDevice(getPendingDevices(), controller)
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

    suspend fun getPendingDevicesCopy(): MutableList<IotDevice> {
        return getPendingDevices().toMutableList()
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

    suspend fun changePendingDevice(device: IotDevice) {
        val storage = CloudStorages.getSmartHomeStorage()
        suspendCoroutine<Unit> { continuation ->
            storage.updatePendingDevice(device,
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

    private suspend fun getPendingDevices() = pendingDevices ?: loadPendingDevices()

    private suspend fun loadPendingDevices(): MutableList<IotDevice> {
        val devices = CloudStorages.loadPendingDevices()
        this.pendingDevices = devices
        return devices
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

    fun saveScript(script: Script) {
        if (scripts.contains(script)) {
            scripts[scripts.indexOf(script)] = script
        } else {
            scripts.add(script)
        }
        _scriptsObservable?.onNext(scripts)
    }

    fun getScript(guid: Long): Script? {
        return scripts.find { it.guid == guid }
    }
}
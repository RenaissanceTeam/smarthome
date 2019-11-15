package smarthome.library.datalibrary

import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import io.reactivex.Observable
import smarthome.library.common.DeviceUpdate
import smarthome.library.common.IotDevice
import smarthome.library.common.SmartHome
import smarthome.library.common.SmartHomeStorage
import smarthome.library.datalibrary.constants.HOMES_NODE
import smarthome.library.datalibrary.constants.HOME_DEVICES_NODE
import smarthome.library.datalibrary.constants.PENDING_DEVICES_NODE
import smarthome.library.datalibrary.util.ObserverMode
import smarthome.library.datalibrary.util.withContinuation
import smarthome.library.datalibrary.util.withObjectContinuation
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirestoreSmartHomeStorage(
    private val homeId: String
) : SmartHomeStorage {

    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val homeRef: DocumentReference by lazy { db.collection(HOMES_NODE).document(homeId) }
    private val devicesRef: CollectionReference by lazy { homeRef.collection(HOME_DEVICES_NODE) }
    private val pendingDevicesRef: CollectionReference by lazy { homeRef.collection(PENDING_DEVICES_NODE) }

    override suspend fun createSmartHome() {
        suspendCoroutine<Unit> {
            db.collection(HOMES_NODE).document(homeId).set(mapOf(Pair("exists", "true"))).withContinuation(it)
        }
    }

    override suspend fun postSmartHome(smartHome: SmartHome) {
        for (device in smartHome.devices) addDevice(device)
    }

    override suspend fun getSmartHome(): SmartHome {
        return suspendCoroutine { continuation ->
            devicesRef.get()
                .addOnSuccessListener { documents ->
                    try {
                        val smartHome = SmartHome()
                        documents.forEach { smartHome.devices.add(it.toObject(IotDevice::class.java)) }

                        continuation.resumeWith(Result.success(smartHome))
                    } catch (e: Throwable) {
                        continuation.resumeWithException(e)
                    }
                }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
    }


    override suspend fun addDevice(iotDevice: IotDevice) {
        suspendCoroutine<Unit> {
            getDeviceRef(iotDevice).set(iotDevice).withContinuation(it)
        }
    }


    override suspend fun updateDevice(device: IotDevice) {
        suspendCoroutine<Unit> {
            getDeviceRef(device).set(device, SetOptions.merge()).withContinuation(it)
        }
    }

    override suspend fun getDevice(guid: Long): IotDevice {
        return suspendCoroutine { getDeviceRef(guid).get().withObjectContinuation(it) }
    }

    override suspend fun removeDevice(iotDevice: IotDevice) {
        suspendCoroutine<Unit> {
            getDeviceRef(iotDevice).delete().withContinuation(it)
        }
    }

    override suspend fun observeDevicesUpdates(): Observable<DeviceUpdate> {
        return observeDeviceUpdates(ObserverMode.DEVICES)
    }

    override suspend fun observePendingDevicesUpdates(): Observable<DeviceUpdate> {
        return observeDeviceUpdates(ObserverMode.PENDING_DEVICES)
    }

    override suspend fun addPendingDevice(iotDevice: IotDevice) {
        suspendCoroutine<Unit> {
            getPendingDeviceRef(iotDevice).set(iotDevice).withContinuation(it)
        }
    }

    override suspend fun updatePendingDevice(device: IotDevice) {
        suspendCoroutine<Unit> {
            getPendingDeviceRef(device).set(device, SetOptions.merge()).withContinuation(it)
        }
    }

    override suspend fun removePendingDevice(iotDevice: IotDevice) {
        suspendCoroutine<Unit> {
            getPendingDeviceRef(iotDevice).delete().withContinuation(it)
        }
    }

    override suspend fun fetchPendingDevices(): MutableList<IotDevice> {
        return suspendCoroutine { continuation ->
            pendingDevicesRef.get()
                .addOnSuccessListener { documents ->
                    try {
                        val pendingDevices = mutableListOf<IotDevice>()
                        for (document in documents)
                            pendingDevices.add(document.toObject(IotDevice::class.java))

                        continuation.resumeWith(Result.success(pendingDevices))
                    } catch (e: Throwable) {
                        continuation.resumeWithException(e)
                    }
                }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
    }

    private fun observeDeviceUpdates(mode: ObserverMode): Observable<DeviceUpdate> {
        return Observable.create<DeviceUpdate> {
            val eventListener: EventListener<QuerySnapshot> = EventListener { snapshot, e ->
                if (e != null) {
                    it.onError(e)
                    return@EventListener
                }

                if (snapshot == null) {
                    it.onError(NullPointerException("null snapshot"))
                    return@EventListener
                }

                val devices = ArrayList<IotDevice>()
                for (doc in snapshot)
                    devices.add(doc.toObject(IotDevice::class.java))

                it.onNext(DeviceUpdate(devices, snapshot.metadata.hasPendingWrites()))
            }

            when (mode) {
                ObserverMode.DEVICES -> devicesRef.addSnapshotListener(eventListener)
                ObserverMode.PENDING_DEVICES -> pendingDevicesRef.addSnapshotListener(eventListener)
            }
        }

    }


    private fun getDeviceRef(iotDevice: IotDevice): DocumentReference {
        return devicesRef.document(iotDevice.id.toString())
    }

    private fun getDeviceRef(guid: Long): DocumentReference {
        return devicesRef.document(guid.toString())
    }

    private fun getPendingDeviceRef(iotDevice: IotDevice): DocumentReference {
        return pendingDevicesRef.document(iotDevice.id.toString())
    }
}
package smarthome.library.datalibrary.store.firestore

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice
import smarthome.library.common.SmartHome
import smarthome.library.datalibrary.constants.FIREBASE_READ_VALUE_ERROR
import smarthome.library.datalibrary.constants.HOMES_NODE
import smarthome.library.datalibrary.constants.HOME_DEVICES_NODE
import smarthome.library.datalibrary.constants.TAG
import smarthome.library.datalibrary.store.SmartHomeStorage
import smarthome.library.datalibrary.store.listeners.DeviceListener
import smarthome.library.datalibrary.store.listeners.DevicesObserver
import smarthome.library.datalibrary.store.listeners.SmartHomeListener

class FirestoreSmartHomeStorage(
    private val homeId: String,
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : SmartHomeStorage {

    private val ref: CollectionReference = db.collection(HOMES_NODE).document(homeId).collection(HOME_DEVICES_NODE)

    private var registration: ListenerRegistration? = null

    override fun createSmartHome(successListener: OnSuccessListener<Void>, failureListener: OnFailureListener) {
        db.collection(HOMES_NODE).document(homeId).set(mapOf(Pair("exists", "true")))
            .addOnSuccessListener(successListener)
            .addOnFailureListener(failureListener)
    }

    override fun postSmartHome(
        smartHome: SmartHome, successListener: OnSuccessListener<Void>,
        failureListener: OnFailureListener
    ) {
        for (device in smartHome.devices)
            addDevice(device)
    }

    override fun getSmartHome(listener: SmartHomeListener, failureListener: OnFailureListener) {
        val smartHome = SmartHome()

        ref.get()
            .addOnSuccessListener { documents ->
                for (document in documents)
                    smartHome.devices.add(document.toObject(IotDevice::class.java))

                listener.onSmartHomeReceived(smartHome)
            }
            .addOnFailureListener { failureListener }
    }

    override fun addDevice(
        iotDevice: IotDevice,
        successListener: OnSuccessListener<Void>,
        failureListener: OnFailureListener
    ) {
        getDeviceRef(iotDevice)
            .set(iotDevice)
            .addOnSuccessListener(successListener)
            .addOnFailureListener(failureListener)
    }

    override fun updateDevice(
        device: IotDevice,
        controller: BaseController?,
        successListener: OnSuccessListener<Void>,
        failureListener: OnFailureListener
    ) {

        if (controller != null && device.getControllers().contains(controller))
            controller.setPending()

        getDeviceRef(device)
            .set(device, SetOptions.merge())
            .addOnSuccessListener(successListener)
            .addOnFailureListener(failureListener)
    }

    override fun getDevice(
        guid: Long,
        listener: DeviceListener,
        failureListener: OnFailureListener
    ) {
        getDeviceRef(guid)
            .get()
            .addOnSuccessListener { document ->
                listener.onDeviceReceived(document.toObject(IotDevice::class.java)!!)
            }
            .addOnFailureListener(failureListener)
    }

    override fun removeDevice(
        iotDevice: IotDevice,
        successListener: OnSuccessListener<Void>,
        failureListener: OnFailureListener
    ) {
        getDeviceRef(iotDevice)
            .delete()
            .addOnSuccessListener(successListener)
            .addOnFailureListener(failureListener)
    }


    override fun observeDevicesUpdates(observer: DevicesObserver) {
        registration = ref.addSnapshotListener(EventListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Devices updates listen failed", e)
                return@EventListener
            }

            if (snapshot == null)
                return@EventListener

            val devices = ArrayList<IotDevice>()
            for (doc in snapshot)
                devices.add(doc.toObject(IotDevice::class.java))

            observer.onDevicesChanged(devices, snapshot.metadata.hasPendingWrites())
        })
    }

    override fun detachDevicesUpdatesObserver() {
        registration?.remove()
    }


    private fun getDeviceRef(iotDevice: IotDevice): DocumentReference {
        return ref.document(iotDevice.guid.toString())
    }

    private fun getDeviceRef(guid: Long): DocumentReference {
        return ref.document(guid.toString())
    }

    companion object {

        private var instance: FirestoreSmartHomeStorage? = null

        fun getInstance(homeId: String): FirestoreSmartHomeStorage? {
            if (instance == null)
                instance =
                    instantiate(homeId)

            return instance
        }

        private fun instantiate(homeId: String): FirestoreSmartHomeStorage? {
            val auth = FirebaseAuth.getInstance()

            auth.currentUser?.let { return FirestoreSmartHomeStorage(homeId) } ?: return null
        }
    }
}
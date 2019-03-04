package ru.smarthome.database.store.firebase

import android.util.Log
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import ru.smarthome.library.BaseController
import ru.smarthome.library.IotDevice
import ru.smarthome.library.SmartHome
import ru.smarthome.database.constants.*
import ru.smarthome.database.store.SmartHomeStorage
import ru.smarthome.database.store.listeners.SmartHomeListener

class FirebaseSmartHomeStorage(
    uid: String,
    db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : SmartHomeStorage {

    private val ref: DocumentReference = db.collection(uid).document(SMART_HOME_REF)

    override fun postSmartHome(
        smartHome: SmartHome, successListener: OnSuccessListener<Void>,
        failureListener: OnFailureListener
    ) {
        ref.set(smartHome)
            .addOnSuccessListener(successListener)
            .addOnFailureListener(failureListener)
    }


    override fun updateSmartHomeDevice(
        device: IotDevice,
        controller: BaseController?,
        successListener: OnSuccessListener<Void>,
        failureListener: OnFailureListener
    ) {

        if (controller != null && device.getControllers().contains(controller))
            controller.setPending()

        ref.update(DEVICES_FIELD_KEY, FieldValue.arrayUnion(device))
            .addOnSuccessListener(successListener)
            .addOnFailureListener(failureListener)
    }

    override fun getSmartHome(listener: SmartHomeListener) {
        ref.get()
            .addOnSuccessListener { res ->
                res.toObject(SmartHome::class.java)?.let { listener.onSmartHomeReceived(it) }
            }
            .addOnFailureListener { exception -> Log.d(javaClass.name, FIREBASE_READ_VALUE_ERROR, exception) }
    }

    companion object {

        private var instance: FirebaseSmartHomeStorage? = null

        fun getInstance(): FirebaseSmartHomeStorage? {
            if (instance == null)
                instance = instantiate()

            return instance
        }

        private fun instantiate(): FirebaseSmartHomeStorage? {
            val auth = FirebaseAuth.getInstance()

            auth.currentUser?.let { return FirebaseSmartHomeStorage(it.uid) } ?: return null
        }
    }
}
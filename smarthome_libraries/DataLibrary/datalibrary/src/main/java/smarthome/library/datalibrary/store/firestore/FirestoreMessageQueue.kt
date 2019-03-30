package smarthome.library.datalibrary.store.firestore

import android.util.Log
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import smarthome.library.common.constants.CHANGE_DEVICE_STATUS
import smarthome.library.common.constants.DISCOVER_ALL
import smarthome.library.common.constants.DISCOVER_DEVICE
import smarthome.library.common.message.ChangeDeviceStatusRequest
import smarthome.library.common.message.DiscoverAllDevicesRequest
import smarthome.library.common.message.DiscoverDeviceRequest
import smarthome.library.common.message.Message
import smarthome.library.datalibrary.BuildConfig.DEBUG
import smarthome.library.datalibrary.constants.HOMES_NODE
import smarthome.library.datalibrary.constants.MESSAGES_NODE
import smarthome.library.datalibrary.constants.TAG
import smarthome.library.datalibrary.constants.WrongMessageType
import smarthome.library.datalibrary.store.MessageQueue
import smarthome.library.datalibrary.store.listeners.MessageListener
import java.lang.IllegalArgumentException

class FirestoreMessageQueue(
    private val homeId: String,
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : MessageQueue {

    private val ref: CollectionReference = db.collection(HOMES_NODE).document(homeId).collection(MESSAGES_NODE)

    private var registration: ListenerRegistration? = null

    override fun postMessage(
        message: Message,
        successListener: OnSuccessListener<Void>,
        failureListener: OnFailureListener
    ) {
        getMessageRef(message)
            .set(message)
            .addOnSuccessListener(successListener)
            .addOnFailureListener(failureListener)
    }

    override fun removeMessage(
        message: Message,
        successListener: OnSuccessListener<Void>,
        failureListener: OnFailureListener
    ) {
        getMessageRef(message)
            .delete()
            .addOnSuccessListener(successListener)
            .addOnFailureListener(failureListener)
    }

    override fun subscribe(listener: MessageListener) {
        registration = ref.addSnapshotListener(EventListener {snapshot, e ->
            if(e != null) {
                Log.w(TAG, "Devices updates listen failed", e)
                return@EventListener
            }

            if (snapshot == null)
                return@EventListener

            val devices = ArrayList<Any>()
            for (doc in snapshot) {
                val messageType: String = doc.get("messageType") as String
                val message = when (messageType) {
                    CHANGE_DEVICE_STATUS -> doc.toObject(ChangeDeviceStatusRequest::class.java)
                    DISCOVER_ALL -> doc.toObject(DiscoverAllDevicesRequest::class.java)
                    DISCOVER_DEVICE -> doc.toObject(DiscoverDeviceRequest::class.java)
                    else -> throw WrongMessageType(messageType)
                }
                devices.add(message)
            }

            listener.onMessagesReceived(devices, snapshot.metadata.hasPendingWrites())
        } )
    }

    private fun getMessageRef(message: Message): DocumentReference {
        return ref.document(message.id)
    }


    companion object {

        private var instance: FirestoreMessageQueue? = null

        fun getInstance(homeId: String): FirestoreMessageQueue? {
            if (instance == null)
                instance =
                    instantiate(homeId)

            return instance
        }

        private fun instantiate(homeId: String): FirestoreMessageQueue? {
            val auth = FirebaseAuth.getInstance()

            auth.currentUser?.let { return FirestoreMessageQueue(homeId) } ?: return null
        }
    }
}
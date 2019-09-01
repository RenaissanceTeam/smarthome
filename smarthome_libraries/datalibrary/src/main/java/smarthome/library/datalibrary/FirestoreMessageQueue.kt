package smarthome.library.datalibrary

import android.util.Log
import com.google.firebase.firestore.*
import smarthome.library.common.MessageListener
import smarthome.library.common.MessageQueue
import smarthome.library.common.constants.CHANGE_DEVICE_STATUS
import smarthome.library.common.constants.DISCOVER_ALL
import smarthome.library.common.constants.DISCOVER_DEVICE
import smarthome.library.common.message.ChangeDeviceStatusRequest
import smarthome.library.common.message.DiscoverAllDevicesRequest
import smarthome.library.common.message.DiscoverDeviceRequest
import smarthome.library.common.message.Message
import smarthome.library.datalibrary.constants.HOMES_NODE
import smarthome.library.datalibrary.constants.MESSAGES_NODE
import smarthome.library.datalibrary.constants.TAG
import smarthome.library.datalibrary.constants.WrongMessageType
import smarthome.library.datalibrary.util.withContinuation
import kotlin.coroutines.suspendCoroutine

class FirestoreMessageQueue(
    private val homeId: String,
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : MessageQueue {

    private val ref: CollectionReference = db.collection(HOMES_NODE).document(homeId).collection(MESSAGES_NODE)

    private var registration: ListenerRegistration? = null


    override suspend fun postMessage(message: Message) {
        suspendCoroutine<Unit> {
            getMessageRef(message).set(message).withContinuation(it)
        }
    }

    override suspend fun removeMessage(message: Message) {
        suspendCoroutine<Unit> {
            getMessageRef(message).delete().withContinuation(it)
        }
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

            listener(devices, snapshot.metadata.hasPendingWrites())
        } )
    }

    private fun getMessageRef(message: Message): DocumentReference {
        return ref.document(message.id)
    }
}
package smarthome.client.data

import smarthome.library.common.IotDevice
import smarthome.library.common.scripts.Script
import smarthome.library.datalibrary.store.listeners.DevicesObserver

class RemoteStorageImpl : RemoteStorage {

//    private var homeStorage: SmartHomeStorage? = null
//    private var instanceTokenStorage: InstanceTokenStorage? = null
//    private var messageQueue: MessageQueue? = null
//    private val userId = FirebaseAuth.getInstance().currentUser?.uid
//
//    private suspend fun getSmartHomeStorage(): SmartHomeStorage {
//        if (homeStorage == null) {
//            setupFirestore()
//        }
//        return homeStorage!!
//    }
//
//    private suspend fun getInstanceTokenStorage(): InstanceTokenStorage {
//        if (instanceTokenStorage == null) {
//            setupFirestore()
//        }
//        return instanceTokenStorage!!
//    }
//
//    suspend fun getMessageQueue(): MessageQueue {
//        if (messageQueue == null) {
//            setupFirestore()
//        }
//        return messageQueue!!
//    }
//
//    private suspend fun setupFirestore() {
//        val userId = this.userId ?: throw AuthenticationFailed()
//
//        val references = FirestoreHomesReferencesStorage(userId)
//        val homeIds = suspendCoroutine<List<String>> { continuation ->
//            references.getHomesReferences(
//                    HomesReferencesListener { continuation.resumeWith(Result.success(it.homes)) },
//                    OnFailureListener { continuation.resumeWith(Result.failure(RemoteFailure(it))) }
//            )
//        }
//        if (homeIds.isNullOrEmpty()) throw NoHomeid()
//        val homeId = homeIds[0] // todo let user choose
//
//        homeStorage = FirestoreSmartHomeStorage(homeId)
//        instanceTokenStorage = FirestoreInstanceTokenStorage(homeId)
//        messageQueue = FirestoreMessageQueue(homeId)
//    }
//
//    suspend fun loadHome(): SmartHome {
//        val storage = getSmartHomeStorage()
//
//        return suspendCoroutine { continuation ->
//            storage.getSmartHome(
//                    SmartHomeListener {
//                        continuation.resumeWith(Result.success(it))
//                    },
//                    OnFailureListener { continuation.resumeWith(Result.failure(RemoteFailure(it))) }
//            )
//        }
//    }
//
//    suspend fun loadPendingDevices(): MutableList<IotDevice> {
//        val storage = getSmartHomeStorage()
//
//        return suspendCoroutine { continuation ->
//            storage.fetchPendingDevices(
//                    PendingDevicesFetchListener {
//                        continuation.resumeWith(Result.success(it))
//                    },
//                    OnFailureListener { continuation.resumeWith(Result.failure(RemoteFailure(it))) }
//            )
//        }
//    }
//
//    suspend fun saveInstanceToken(token: String) {
//        val userId = this.userId ?: throw AuthenticationFailed()
//        val storage = getInstanceTokenStorage()
//        suspendCoroutine<Unit> { continuation ->
//            storage.saveToken(userId, token,
//                    OnSuccessListener { continuation.resumeWith(Result.success(Unit)) },
//                    OnFailureListener { continuation.resumeWith(Result.failure(it)) })
//        }
//    }


    override suspend fun observeDevices(devicesObserver: DevicesObserver) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun saveAppToken(newToken: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getAppToken(): String? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun updateDevice(device: IotDevice) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun updatePendingDevice(device: IotDevice) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun saveScript(script: Script) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
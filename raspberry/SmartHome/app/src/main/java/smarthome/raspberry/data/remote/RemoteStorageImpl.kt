package smarthome.raspberry.data.remote

import smarthome.library.common.BaseController
import smarthome.library.datalibrary.store.InstanceTokenStorage
import smarthome.library.datalibrary.store.MessageQueue
import smarthome.library.datalibrary.store.SmartHomeStorage
import smarthome.library.datalibrary.store.firestore.FirestoreHomesReferencesStorage
import smarthome.library.datalibrary.store.firestore.FirestoreInstanceTokenStorage
import smarthome.library.datalibrary.store.firestore.FirestoreMessageQueue
import smarthome.library.datalibrary.store.firestore.FirestoreSmartHomeStorage
import smarthome.raspberry.data.RemoteStorage

class RemoteStorageImpl : RemoteStorage {

    private var homeStorage: SmartHomeStorage? = null
    private var instanceTokenStorage: InstanceTokenStorage? = null
    private var messageQueue: MessageQueue? = null
    private val input: RemoteStorageInput = TODO()

    override suspend fun init() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun onControllerChanged(controller: BaseController) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun createHome(homeId: String) {
        getSmartHomeStorage()
        getSmartHomeStorage().createSmartHome()
    }

    private suspend fun getSmartHomeStorage(): SmartHomeStorage {
        if (homeStorage == null) {
            setupFirestore()
        }
        return homeStorage!!
    }

    private suspend fun getInstanceTokenStorage(): InstanceTokenStorage {
        if (instanceTokenStorage == null) {
            setupFirestore()
        }
        return instanceTokenStorage!!
    }

//    suspend fun getMessageQueue(): MessageQueue {
//        if (messageQueue == null) {
//            setupFirestore()
//        }
//        return messageQueue!!
//    }

    private suspend fun setupFirestore() {
        val userId = input.getUserId()
        val references = FirestoreHomesReferencesStorage(userId)


        val homeIds = references.getHomesReferences().homes
        if (homeIds.isNullOrEmpty()) TODO()

        val homeId = input.getHomeId()

        homeStorage = FirestoreSmartHomeStorage(homeId)
        instanceTokenStorage = FirestoreInstanceTokenStorage(homeId)
        messageQueue = FirestoreMessageQueue(homeId)
    }
}
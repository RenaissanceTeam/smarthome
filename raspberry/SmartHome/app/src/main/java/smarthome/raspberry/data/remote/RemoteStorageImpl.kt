package smarthome.raspberry.data.remote

import smarthome.library.common.BaseController
import smarthome.library.common.HomesReferencesStorage
import smarthome.library.common.InstanceTokenStorage
import smarthome.library.common.MessageQueue
import smarthome.library.datalibrary.store.SmartHomeStorage
import smarthome.raspberry.data.RemoteStorage

class RemoteStorageImpl : RemoteStorage {

    private val homeStorage: SmartHomeStorage = TODO()
    private val instanceTokenStorage: InstanceTokenStorage = TODO()
    private val messageQueue: MessageQueue = TODO()
    private val input: RemoteStorageInput = TODO()
    private val homesReferencesStorage: HomesReferencesStorage = TODO()

    override suspend fun init() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun onControllerChanged(controller: BaseController) {
        TODO()
    }

    override suspend fun createHome(homeId: String) {
        homesReferencesStorage.addHomeReference(homeId)
        homeStorage.createSmartHome()
    }

    override suspend fun isHomeIdUnique(homeId: String): Boolean {
        val exists = homesReferencesStorage.checkIfHomeExists(homeId)

        return !exists
    }

    //    suspend fun getMessageQueue(): MessageQueue {
//        if (messageQueue == null) {
//            setupFirestore()
//        }
//        return messageQueue!!
//    }

}
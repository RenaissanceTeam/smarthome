package smarthome.client.data

import io.reactivex.Observable
import org.koin.core.KoinComponent
import org.koin.core.inject
import smarthome.client.domain.usecases.AuthenticationUseCase
import smarthome.client.domain.usecases.HomeUseCases
import smarthome.client.util.NoHomeid
import smarthome.library.common.IotDevice
import smarthome.library.common.scripts.Script
import smarthome.library.datalibrary.store.InstanceTokenStorage
import smarthome.library.datalibrary.store.MessageQueue
import smarthome.library.datalibrary.store.SmartHomeStorage
import smarthome.library.datalibrary.store.firestore.FirestoreHomesReferencesStorage
import smarthome.library.datalibrary.store.firestore.FirestoreInstanceTokenStorage
import smarthome.library.datalibrary.store.firestore.FirestoreMessageQueue
import smarthome.library.datalibrary.store.firestore.FirestoreSmartHomeStorage
import smarthome.library.datalibrary.store.listeners.DeviceUpdate

class RemoteStorageImpl : RemoteStorage, KoinComponent {

    private var homeStorage: SmartHomeStorage? = null
    private var instanceTokenStorage: InstanceTokenStorage? = null
    private var messageQueue: MessageQueue? = null
    private val authenticationUseCase: AuthenticationUseCase by inject()
    private val homeUseCases: HomeUseCases by inject()

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
        val userId = authenticationUseCase.getUserId() ?: throw RuntimeException("no user id to setup remote storage")
        val references = FirestoreHomesReferencesStorage(userId)

        val homeIds = references.getHomesReferences().homes
        if (homeIds.isNullOrEmpty()) throw NoHomeid()

        val homeId = homeUseCases.getChosenHomeId()

        homeStorage = FirestoreSmartHomeStorage(homeId)
        instanceTokenStorage = FirestoreInstanceTokenStorage(homeId)
        messageQueue = FirestoreMessageQueue(homeId)
    }



    override suspend fun observeDevices(): Observable<DeviceUpdate> {
        return getSmartHomeStorage().observeDevicesUpdates()
    }

    override suspend fun saveAppToken(newToken: String) {
        val userId = authenticationUseCase.getUserId() ?: throw RuntimeException("no user id to setup remote storage")
        getInstanceTokenStorage().saveToken(userId, newToken)
    }

    override suspend fun updateDevice(device: IotDevice) {
        getSmartHomeStorage().updateDevice(device)
    }

    override suspend fun updatePendingDevice(device: IotDevice) {
        getSmartHomeStorage().updatePendingDevice(device)
    }

    override suspend fun saveScript(script: Script) {
//        getSmartHomeStorage().saves
    }
}
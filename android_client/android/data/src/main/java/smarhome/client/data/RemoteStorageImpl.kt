package smarhome.client.data

import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Observable
import org.koin.core.KoinComponent
import org.koin.core.inject
import smarthome.library.common.*
import smarthome.library.common.scripts.Script


class RemoteStorageImpl(
        private val instanceTokenStorageFactory: (homeId: String) -> InstanceTokenStorage,
        private val homesReferencesStorageFactory: (uid: String) -> HomesReferencesStorage,
        private val homeStorageFactory: (homeId: String) -> SmartHomeStorage
) : RemoteStorage, KoinComponent {
    private var homeStorage: SmartHomeStorage? = null
    private var instanceTokenStorage: InstanceTokenStorage? = null
    private var homesReferencesStorage: HomesReferencesStorage? = null
    private var messageQueue: MessageQueue? = null
    private val input: RemoteStorageInput by inject()


//    suspend fun getMessageQueue(): MessageQueue {
//        if (messageQueue == null) {
//            setupFirestore()
//        }
//        return messageQueue!!
//    }

    private suspend fun setupFirestore() {
        homesReferencesStorage ?: initializeHomesReferencesStorage()
        homesReferencesStorage ?: throw RuntimeException("can't initialize homesreferences storage")

        homesReferencesStorage?.let { initializeInstanceTokenStorage(it) }
    }
    private suspend fun initializeInstanceTokenStorage(homesReferencesStorage: HomesReferencesStorage) {


        val homeIds = homesReferencesStorage.getHomesReferences().homes
        if (homeIds.isNullOrEmpty()) throw NoHomeid()

        val homeId = input.chooseHomeId(homeIds)
        instanceTokenStorage = instanceTokenStorageFactory(homeId)
        homeStorage = homeStorageFactory(homeId)
    }

    private fun initializeHomesReferencesStorage() {
        val uid = FirebaseAuth.getInstance().uid ?: return
        homesReferencesStorage = homesReferencesStorageFactory(uid)
    }

    override suspend fun getAllHomeIds(): MutableList<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun observeDevices(): Observable<DeviceUpdate> {
        return getSmartHomeStorage().observeDevicesUpdates()
    }

    override suspend fun saveAppToken(newToken: String) {
        getInstanceTokenStorage().saveToken(input.getUserId(), newToken)
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
}

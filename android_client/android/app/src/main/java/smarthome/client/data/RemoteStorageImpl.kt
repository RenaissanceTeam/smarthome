package smarthome.client.data

import io.reactivex.Observable
import org.koin.core.KoinComponent
import org.koin.core.inject
import smarthome.client.util.NoHomeid
import smarthome.library.common.*
import smarthome.library.common.scripts.Script


class RemoteStorageImpl : RemoteStorage, KoinComponent {

    private var homeStorage: SmartHomeStorage? = null
    private var instanceTokenStorage: InstanceTokenStorage = TODO()
    private var homesReferencesStorage: HomesReferencesStorage = TODO()
    private var messageQueue: MessageQueue? = null
    private val input: RemoteStorageInput by inject()

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

        val homeIds = homesReferencesStorage.getHomesReferences().homes
        if (homeIds.isNullOrEmpty()) throw NoHomeid()

        val homeId = input.chooseHomeId(homeIds)

        // todo setup storages with userid
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
}
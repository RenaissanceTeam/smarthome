package smarthome.raspberry.data.remote

import io.reactivex.Observable
import smarthome.library.common.DeviceUpdate
import smarthome.library.common.HomesReferencesStorage
import smarthome.library.common.IotDevice
import smarthome.library.common.SmartHomeStorage
import smarthome.raspberry.data.RemoteStorage
import smarthome.raspberry.data.RemoteStorageInput

class RemoteStorageImpl(
        private val input: RemoteStorageInput,
        private val homeStorageFactory: (String) -> SmartHomeStorage,
        private val homesReferencesStorageFactory: (String) -> HomesReferencesStorage) :
        RemoteStorage {

    private var homeStorage: SmartHomeStorage? = null
    private var homesReferencesStorage: HomesReferencesStorage? = null

    private suspend fun getHomeStorage(): SmartHomeStorage {
        if (homeStorage == null) {
            homeStorage = homeStorageFactory(input.getHomeId())
        }
        return homeStorage!!
    }

    private suspend fun getHomesReferenecesStorage(): HomesReferencesStorage {
        if (homesReferencesStorage == null) {
            homesReferencesStorage = homesReferencesStorageFactory(input.getUserId())
        }
        return homesReferencesStorage!!
    }


    override suspend fun updateDevice(device: IotDevice) {
        getHomeStorage().updateDevice(device)
    }

    override suspend fun createHome(homeId: String) {
        getHomesReferenecesStorage().addHomeReference(homeId)
        getHomeStorage().createSmartHome()
    }

    override suspend fun isHomeIdUnique(homeId: String): Boolean {
        val exists = getHomesReferenecesStorage().checkIfHomeExists(homeId)

        return !exists
    }

    override suspend fun getDevices(): Observable<DeviceUpdate> {
        return getHomeStorage().observeDevicesUpdates()
    }

    //    suspend fun getMessageQueue(): MessageQueue {
//        if (messageQueue == null) {
//            setupFirestore()
//        }
//        return messageQueue!!
//    }

    override suspend fun addPendingDevice(device: IotDevice) {
        getHomeStorage().addPendingDevice(device)
    }

    override suspend fun removePendingDevice(device: IotDevice) {
        getHomeStorage().removePendingDevice(device)
    }

    override suspend fun addDevice(device: IotDevice) {
        getHomeStorage().addDevice(device)
    }

    override suspend fun removeDevice(device: IotDevice) {
        getHomeStorage().removeDevice(device)
    }
}
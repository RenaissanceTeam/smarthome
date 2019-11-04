package smarthome.raspberry.data.remote

import io.reactivex.disposables.Disposable
import smarthome.library.common.HomesReferencesStorage
import smarthome.library.common.IotDevice
import smarthome.library.common.SmartHomeStorage
import smarthome.raspberry.home_api.data.HomeInfoSource
import smarthome.raspberry.data.RemoteStorage

class RemoteStorageImpl(
        input: smarthome.raspberry.home_api.data.HomeInfoSource,
        private val homeStorageFactory: (String) -> SmartHomeStorage,
        private val homesReferencesStorageFactory: (String) -> HomesReferencesStorage) :
        RemoteStorage {

    private var homeStorage: SmartHomeStorage? = null
    private var homesReferencesStorage: HomesReferencesStorage? = null
    private var uidSubscription: Disposable? = null
    private var homeRefSubscription: Disposable? = null

    init {
        uidSubscription = input.getObservableUserId().subscribe {
            homesReferencesStorage = homesReferencesStorageFactory(it)
        }
        homeRefSubscription = input.getObservableHomeId().subscribe {
            homeStorage = homeStorageFactory(it)
        }
    }

    override suspend fun updateDevice(device: IotDevice) {
        homeStorage?.updateDevice(device)
    }

    override suspend fun saveHome(homeId: String) {
        homesReferencesStorage?.addHomeReference(homeId)
        homeStorage?.createSmartHome()
    }

    override suspend fun isHomeIdUnique(homeId: String): Boolean {
        val exists = homesReferencesStorage?.checkIfHomeExists(homeId) ?: true

        return !exists
    }

    //    suspend fun getMessageQueue(): MessageQueue {
//        if (messageQueue == null) {
//            setupFirestore()
//        }
//        return messageQueue!!
//    }

    override suspend fun addPendingDevice(device: IotDevice) {
        homeStorage?.addPendingDevice(device)
    }

    override suspend fun removePendingDevice(device: IotDevice) {
        homeStorage?.removePendingDevice(device)
    }

    override suspend fun addDevice(device: IotDevice) {
        homeStorage?.addDevice(device)
    }

    override suspend fun removeDevice(device: IotDevice) {
        homeStorage?.removeDevice(device)
    }
}
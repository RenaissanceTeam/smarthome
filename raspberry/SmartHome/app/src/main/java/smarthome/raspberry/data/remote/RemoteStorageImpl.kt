package smarthome.raspberry.data.remote

import io.reactivex.Observable
import smarthome.library.common.*
import smarthome.raspberry.data.RemoteStorage

class RemoteStorageImpl : RemoteStorage {

    private val homeStorage: SmartHomeStorage = TODO()
    private val instanceTokenStorage: InstanceTokenStorage = TODO()
    private val messageQueue: MessageQueue = TODO()
    private val homesReferencesStorage: HomesReferencesStorage = TODO()

    override suspend fun updateDevice(device: IotDevice) {
        homeStorage.updateDevice(device)
    }

    override suspend fun createHome(homeId: String) {
        homesReferencesStorage.addHomeReference(homeId)
        homeStorage.createSmartHome()
    }

    override suspend fun isHomeIdUnique(homeId: String): Boolean {
        val exists = homesReferencesStorage.checkIfHomeExists(homeId)

        return !exists
    }

    override suspend fun getDevices(): Observable<DeviceUpdate> {
        return homeStorage.observeDevicesUpdates()
    }

    //    suspend fun getMessageQueue(): MessageQueue {
//        if (messageQueue == null) {
//            setupFirestore()
//        }
//        return messageQueue!!
//    }

    override suspend fun addPendingDevice(device: IotDevice) {
        homeStorage.addPendingDevice(device)
    }

    override suspend fun removePendingDevice(device: IotDevice) {
        homeStorage.removePendingDevice(device)
    }

    override suspend fun addDevice(device: IotDevice) {
        homeStorage.addDevice(device)
    }

    override suspend fun removeDevice(device: IotDevice) {
        homeStorage.removeDevice(device)
    }
}
package smarthome.raspberry.data

import smarthome.raspberry.domain.HomeRepository

class HomeRepositoryImpl : HomeRepository {

    private val localStorage: LocalStorage = TODO()
    private val remoteStorage: RemoteStorage = TODO()

    override suspend fun setupUserInteraction() {
        remoteStorage.init()
    }

    override suspend fun setupDevicesInteraction() {
        // get registered device types
        // understand which resources are needed
        // init these resources
    }
}
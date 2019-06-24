package smarthome.raspberry.data

import smarthome.library.common.BaseController
import smarthome.library.common.ControllerState
import smarthome.library.common.DeviceChannel
import smarthome.raspberry.domain.HomeRepository

class HomeRepositoryImpl : HomeRepository {

    private val localStorage: LocalStorage = TODO()
    private val remoteStorage: RemoteStorage = TODO()
    private val deviceChannels: List<DeviceChannel> = TODO()

    override suspend fun setupUserInteraction() {
        remoteStorage.init()
    }

    override suspend fun setupDevicesInteraction() {
//        deviceChannels.forEach { it.init() }
    }

    override suspend fun setControllerState(controller: BaseController, state: ControllerState) {
        val channel = findSuitableChannel(controller)
        val newState = channel.writeState(controller, state)

        controller.state = newState
        remoteStorage.onControllerChanged(controller)
    }

    private fun findSuitableChannel(controller: BaseController): DeviceChannel {
        TODO()
    }

    override suspend fun fetchControllerState(controller: BaseController): ControllerState {
        val channel = findSuitableChannel(controller)
        val newState = channel.read(controller)

        controller.state = newState
        remoteStorage.onControllerChanged(controller)

        return newState
    }

    override suspend fun onControllerChanged(controller: BaseController) {
        remoteStorage.onControllerChanged(controller)
    }
}
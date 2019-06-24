package smarthome.raspberry.data

import smarthome.library.common.*
import smarthome.raspberry.data.local.LocalStorageOutput
import smarthome.raspberry.data.remote.RemoteStorageInput
import smarthome.raspberry.domain.HomeRepository
import smarthome.raspberry.domain.usecases.ControllersUseCase
import smarthome.raspberry.domain.usecases.DevicesUseCase

class HomeRepositoryImpl : HomeRepository, DeviceChannelOutput, RemoteStorageInput, LocalStorageOutput {

    private val localStorage: LocalStorage = TODO()
    private val remoteStorage: RemoteStorage = TODO()
    private val deviceChannels: List<DeviceChannel> = TODO()
    private val devicesUseCase: DevicesUseCase = TODO()
    private val controllersUseCase: ControllersUseCase = TODO()

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

    override suspend fun onNewDevice(device: IotDevice) {
        devicesUseCase.addNewDevice(device)
    }

    override suspend fun onNewState(controller: BaseController) {
        controllersUseCase.notifyControllerChanged(controller)
    }

    override suspend fun getUserId(): String {

    }

    override suspend fun getHomeId(): String {
        localStorage.getHomeId()
    }

    override suspend fun createHome(homeId: String) {
        remoteStorage.createHome(homeId)
    }
}
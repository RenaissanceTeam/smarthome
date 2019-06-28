package smarthome.raspberry.data

import android.annotation.SuppressLint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import smarthome.library.common.*
import smarthome.raspberry.data.local.LocalStorageInput
import smarthome.raspberry.data.local.LocalStorageOutput
import smarthome.raspberry.data.remote.RemoteStorageInput
import smarthome.raspberry.domain.HomeRepository
import smarthome.raspberry.domain.usecases.ControllersUseCase
import smarthome.raspberry.domain.usecases.DevicesUseCase
import smarthome.raspberry.domain.usecases.HomeUseCase

class HomeRepositoryImpl : HomeRepository, DeviceChannelOutput, RemoteStorageInput,
        LocalStorageInput, LocalStorageOutput {

    private val localStorage: LocalStorage = TODO()
    private val remoteStorage: RemoteStorage = TODO()
    private val deviceChannels: List<DeviceChannel> = TODO()
    private val devicesUseCase: DevicesUseCase = TODO()
    private val homeUseCase: HomeUseCase = TODO()
    private val controllersUseCase: ControllersUseCase = TODO()
    private val ioScope = CoroutineScope(Dispatchers.IO)

    override suspend fun setupUserInteraction() {
        remoteStorage.init()
    }

    @SuppressLint("CheckResult")
    override suspend fun setupDevicesInteraction() {
//        deviceChannels.forEach { it.init() }

        remoteStorage.getDevices().subscribe {
            if (it.isInnerCall) return@subscribe

            ioScope.launch {
                devicesUseCase.onUserRequest(it.devices)
            }
        }
    }

    override suspend fun saveDevice(device: IotDevice) {
        localStorage.updateDevice(device)
        remoteStorage.updateDevice(device)
    }

    override suspend fun proceedReadController(controller: BaseController): BaseController {
        val channel = findSuitableChannel(controller)
        val newState = channel.read(controller)

        controller.state = newState
        return controller
    }

    override suspend fun proceedWriteController(controller: BaseController, state: ControllerState): BaseController {
        val channel = findSuitableChannel(controller)
        val newState = channel.writeState(controller, state)

        controller.state = newState
        return controller
    }

    override fun getCurrentDevices(): MutableList<IotDevice> {
        return localStorage.getDevices()
    }

    override suspend fun generateHomeId(): String {
        return homeUseCase.generateUniqueHomeId()
    }

    override suspend fun isHomeIdUnique(homeId: String): Boolean {
        return remoteStorage.isHomeIdUnique(homeId)
    }

    private fun findSuitableChannel(controller: BaseController): DeviceChannel {
        TODO()
    }

    override suspend fun addPendingDevice(device: IotDevice) {
        localStorage.addPendingDevice(device)
        remoteStorage.addPendingDevice(device)
    }

    override suspend fun onControllerChanged(controller: BaseController) {
        val device = localStorage.findDevice(controller)
        remoteStorage.updateDevice(device)
    }

    override suspend fun onNewDevice(device: IotDevice) {
        devicesUseCase.addNewDevice(device)
    }

    override suspend fun onNewState(controller: BaseController) {
        controllersUseCase.notifyControllerChanged(controller)
    }

    override suspend fun getUserId(): String {
        TODO()
    }

    override suspend fun getHomeId(): String {
        return localStorage.getHomeId()
    }

    override suspend fun createHome(homeId: String) {
        remoteStorage.createHome(homeId)
    }

    override suspend fun removePendingDevice(device: IotDevice) {
        localStorage.removePendingDevice(device)
        remoteStorage.removePendingDevice(device)
    }

    override suspend fun addDevice(device: IotDevice) {
        localStorage.addDevice(device)
        remoteStorage.addDevice(device)
    }

    override suspend fun removeDevice(device: IotDevice) {
        localStorage.removeDevice(device)
        remoteStorage.removeDevice(device)
    }
}
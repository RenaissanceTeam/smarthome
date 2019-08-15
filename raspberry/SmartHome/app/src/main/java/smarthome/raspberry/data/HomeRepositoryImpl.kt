package smarthome.raspberry.data

import android.annotation.SuppressLint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import smarthome.library.common.*
import smarthome.raspberry.data.local.LocalStorageInput
import smarthome.raspberry.data.local.LocalStorageOutput
import smarthome.raspberry.domain.HomeRepository
import smarthome.raspberry.domain.NoControllerException
import smarthome.raspberry.domain.NoDeviceException
import smarthome.raspberry.domain.usecases.ControllersUseCase
import smarthome.raspberry.domain.usecases.DevicesUseCase
import smarthome.raspberry.domain.usecases.HomeUseCase
import java.lang.reflect.Type

class HomeRepositoryImpl(private val localStorage: LocalStorage,
                         private val remoteStorage: RemoteStorage,
                         private val deviceChannels: Map<Type, DeviceChannel>) : HomeRepository, DeviceChannelOutput,
        LocalStorageInput, LocalStorageOutput {

    private lateinit var devicesUseCase: DevicesUseCase
    private lateinit var homeUseCase: HomeUseCase
    private lateinit var controllersUseCase: ControllersUseCase
    private val ioScope = CoroutineScope(Dispatchers.IO)

    override suspend fun setupUserInteraction() {
        remoteStorage.init()
    }

    override suspend fun saveDevice(device: IotDevice) {
        localStorage.updateDevice(device)
        remoteStorage.updateDevice(device)
    }

    override suspend fun proceedReadController(controller: BaseController): BaseController {
        val device = localStorage.findDevice(controller)
        val channel = findSuitableChannel(device)
        val newState = channel.read(device, controller)

        controller.state = newState
        return controller
    }

    override suspend fun proceedWriteController(controller: BaseController, state: ControllerState): BaseController {
        val device = localStorage.findDevice(controller)
        val channel = findSuitableChannel(device)
        val newState = channel.writeState(device, controller, state)

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

    private fun findSuitableChannel(device: IotDevice): DeviceChannel {
        return deviceChannels[device.javaClass]
                ?: throw IllegalArgumentException("no channel for $device")
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


    override suspend fun findController(guid: Long): BaseController {
        val devices = localStorage.getDevices()

        for (device in devices) {
            return device.controllers.find { it.guid == guid } ?: continue
        }
        throw NoControllerException()
    }

    override suspend fun findDevice(controller: BaseController): IotDevice {
        val devices = localStorage.getDevices()


        for (device in devices) {
            if (device.controllers.contains(controller)) return device
        }
        throw NoDeviceException()
    }
}
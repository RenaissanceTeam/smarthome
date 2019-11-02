package smarthome.raspberry.data

import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import smarthome.library.common.*
import smarthome.raspberry.authentication_api.AuthRepo
import smarthome.raspberry.domain.HomeRepository
import smarthome.raspberry.domain.NoControllerException
import smarthome.raspberry.domain.NoDeviceException
import smarthome.raspberry.domain.models.HomeInfo
import smarthome.raspberry.domain.usecases.ControllersUseCase
import smarthome.raspberry.domain.usecases.DevicesUseCase
import smarthome.raspberry.domain.usecases.HomeUseCase

class HomeRepositoryImpl(
        localStorageFactory: (LocalStorageInput, LocalStorageOutput) -> LocalStorage,
        devicesUseCaseFactory: (HomeRepository) -> DevicesUseCase,
        homeUseCaseFactory: (HomeRepository) -> HomeUseCase,
        controllersUseCaseFactory: (HomeRepository) -> ControllersUseCase,
        remoteStorageFactory: (HomeInfoSource) -> RemoteStorage,
        deviceChannelsFactories: Map<String, (DeviceChannelOutput) -> DeviceChannel>,
        private val authRepo: smarthome.raspberry.authentication_api.AuthRepo
) : HomeRepository,
        DeviceChannelOutput,
        LocalStorageInput, LocalStorageOutput, HomeInfoSource {

    private val devicesUseCase = devicesUseCaseFactory(this)
    private val homeUseCase = homeUseCaseFactory(this)
    private val controllersUseCase = controllersUseCaseFactory(this)
    private val localStorage = localStorageFactory(this, this)
    private val deviceChannels: Map<String, DeviceChannel> =
            deviceChannelsFactories.mapValues { it.value(this) }
    private val remoteStorage = remoteStorageFactory(this)

    override suspend fun saveDevice(device: IotDevice) {
        localStorage.updateDevice(device)
        remoteStorage.updateDevice(device)
    }

    override suspend fun createHome(homeId: String) {
        localStorage.saveHome(homeId)
        remoteStorage.saveHome(homeId)
    }

    override suspend fun proceedReadController(controller: BaseController): BaseController {
        val device = localStorage.findDevice(controller)
        val channel = findSuitableChannel(device)
        val newState = channel.read(device, controller)

        controller.state = newState
        return controller
    }

    override fun getObservableUserId(): Observable<String> {
        return authRepo.getUserId()
    }

    override fun getObservableHomeId(): Observable<String> {
        return localStorage.getHomeId()
    }

    override fun getHomeInfo(): Observable<HomeInfo> {
        return Observables.combineLatest(getObservableUserId().startWith(""),
                getObservableHomeId().startWith(""), ::HomeInfo)
    }

    override suspend fun proceedWriteController(controller: BaseController,
                                                state: ControllerState): BaseController {
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
        return deviceChannels[device.javaClass.simpleName]
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

    override suspend fun saveHome(homeId: String) {
        localStorage.saveHome(homeId)
        remoteStorage.saveHome(homeId)
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
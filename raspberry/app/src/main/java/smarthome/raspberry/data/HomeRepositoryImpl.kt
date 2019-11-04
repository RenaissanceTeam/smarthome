package smarthome.raspberry.data

import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import smarthome.library.common.*
import smarthome.raspberry.authentication_api.AuthRepo
import smarthome.raspberry.domain.HomeRepository
import smarthome.raspberry.domain.NoControllerException
import smarthome.raspberry.domain.NoDeviceException
import smarthome.raspberry.entity.HomeInfo
import smarthome.raspberry.controllers.ControllersUseCase
import smarthome.raspberry.devices.DevicesUseCase
import smarthome.raspberry.home.HomeUseCase

class HomeRepositoryImpl(
        localStorageFactory: (LocalStorageInput, LocalStorageOutput) -> LocalStorage,
        devicesUseCaseFactory: (HomeRepository) -> smarthome.raspberry.devices.DevicesUseCase,
        homeUseCaseFactory: (HomeRepository) -> smarthome.raspberry.home.HomeUseCase,
        controllersUseCaseFactory: (HomeRepository) -> smarthome.raspberry.controllers.ControllersUseCase,
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

    override suspend fun createHome(homeId: String) {
        localStorage.saveHome(homeId)
        remoteStorage.saveHome(homeId)
    }


    override fun getObservableUserId(): Observable<String> {
        return authRepo.getUserId()
    }

    override fun getObservableHomeId(): Observable<String> {
        return localStorage.getHomeId()
    }

    override fun getHomeInfo(): Observable<smarthome.raspberry.entity.HomeInfo> {
        return Observables.combineLatest(getObservableUserId().startWith(""),
                getObservableHomeId().startWith(""), ::HomeInfo)
    }

    override suspend fun generateHomeId(): String {
        return homeUseCase.generateUniqueHomeId()
    }

    override suspend fun isHomeIdUnique(homeId: String): Boolean {
        return remoteStorage.isHomeIdUnique(homeId)
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
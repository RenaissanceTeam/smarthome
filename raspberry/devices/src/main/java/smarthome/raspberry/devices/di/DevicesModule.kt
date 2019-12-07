package smarthome.raspberry.devices.di

import org.koin.dsl.module
import org.koin.experimental.builder.factoryBy
import org.koin.experimental.builder.singleBy
import smarthome.raspberry.devices.api.domain.*
import smarthome.raspberry.devices.data.DevicesRepository
import smarthome.raspberry.devices.data.DevicesRepositoryImpl
import smarthome.raspberry.devices.data.storage.LocalStorage
import smarthome.raspberry.devices.data.storage.LocalStorageImpl
import smarthome.raspberry.devices.data.storage.RemoteStorage
import smarthome.raspberry.devices.data.storage.RemoteStorageImpl
import smarthome.raspberry.devices.domain.*

private val domain = module {
    factoryBy<AcceptPendingDeviceUseCase, AcceptPendingDeviceUseCaseImpl>()
    factoryBy<AddDeviceUseCase, AddDeviceUseCaseImpl>()
    factoryBy<GetDeviceByControllerUseCase, GetDeviceByControllerUseCaseImpl>()
    factoryBy<GetDeviceByIdUseCase, GetDeviceByIdUseCaseImpl>()
    factoryBy<GetDevicesUseCase, GetDevicesUseCaseImpl>()
    factoryBy<RemoveDeviceUseCase, RemoveDeviceUseCaseImpl>()
    factoryBy<SaveDeviceUseCase, SaveDeviceUseCaseImpl>()
}

private val data = module {
    singleBy<DevicesRepository, DevicesRepositoryImpl>()
    singleBy<LocalStorage, LocalStorageImpl>()
    factoryBy<RemoteStorage, RemoteStorageImpl>()
}

val devicesModule = listOf(domain, data)

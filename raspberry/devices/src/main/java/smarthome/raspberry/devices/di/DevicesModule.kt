package smarthome.raspberry.devices.di

import org.koin.dsl.module
import org.koin.experimental.builder.factoryBy
import smarthome.raspberry.devices.api.domain.*
import smarthome.raspberry.devices.domain.*

private val domain = module {
    factoryBy<AcceptPendingDeviceUseCase, AcceptPendingDeviceUseCaseImpl>()
    factoryBy<GetDeviceByControllerUseCase, GetDeviceByControllerUseCaseImpl>()
    factoryBy<GetDeviceByIdUseCase, GetDeviceByIdUseCaseImpl>()
    factoryBy<GetDevicesUseCase, GetDevicesUseCaseImpl>()
    factoryBy<RemoveDeviceUseCase, RemoveDeviceUseCaseImpl>()
    factoryBy<SaveDeviceUseCase, SaveDeviceUseCaseImpl>()
}

private val data = module {
}

val devicesModule = listOf(domain, data)

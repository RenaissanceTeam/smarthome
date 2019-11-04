package smarthome.raspberry.di

import com.google.gson.typeadapters.RuntimeTypeAdapterFactory
import org.koin.dsl.module
import smarthome.library.common.BaseController
import smarthome.library.common.DeviceChannelOutput
import smarthome.library.datalibrary.FirestoreHomesReferencesStorage
import smarthome.library.datalibrary.FirestoreSmartHomeStorage
import smarthome.raspberry.arduinodevices.ArduinoDevice
import smarthome.raspberry.arduinodevices.ArduinoDeviceChannel
import smarthome.raspberry.data.*
import smarthome.raspberry.devices.data.storage.LocalDevicesStorage
import smarthome.raspberry.data.local.LocalStorageImpl
import smarthome.raspberry.util.SharedPreferencesHelper
import smarthome.raspberry.devices.data.storage.LocalDevicesStorageImpl
import smarthome.raspberry.data.remote.RemoteStorageImpl
import smarthome.raspberry.authentication_api.AuthRepo
import smarthome.raspberry.domain.HomeRepository
import smarthome.raspberry.authentication.AuthUseCase
import smarthome.raspberry.controllers.ControllersUseCase
import smarthome.raspberry.devices.DevicesUseCase
import smarthome.raspberry.home.HomeUseCase
import smarthome.raspberry.input.InputController
import smarthome.raspberry.input.InputControllerDataSource
import smarthome.raspberry.input.datasource.InputFromSharedDatabase


val dataModule = module {
    factory { smarthome.raspberry.util.SharedPreferencesHelper(get()) }

    val typeAdapter = RuntimeTypeAdapterFactory.of(BaseController::class.java)

    factory { smarthome.raspberry.devices.data.storage.LocalDevicesStorageImpl(
            typeAdapter) as smarthome.raspberry.devices.data.storage.LocalDevicesStorage
    }
    factory { LocalStorageImpl(get(), get()) as LocalStorage }
    val deviceChannels = mapOf(
            ArduinoDevice::class.java.simpleName to { output: DeviceChannelOutput ->
                ArduinoDeviceChannel(output)
            }
    )

    single { smarthome.raspberry.input.InputController(get(), get(), get()) }
    factory {
        InputFromSharedDatabase(get(),
                { FirestoreSmartHomeStorage(it) }) as smarthome.raspberry.input.InputControllerDataSource
    }
    single {
        HomeRepositoryImpl(
                localStorageFactory = { input, output ->
                    LocalStorageImpl(get(), get())
                },
                devicesUseCaseFactory = { smarthome.raspberry.devices.DevicesUseCase(it) },
                homeUseCaseFactory = { smarthome.raspberry.home.HomeUseCase(it) },
                controllersUseCaseFactory = { smarthome.raspberry.controllers.ControllersUseCase(it) },
                deviceChannelsFactories = deviceChannels,
                remoteStorageFactory = {
                    RemoteStorageImpl(it,
                            { homeId -> FirestoreSmartHomeStorage(homeId) },
                            { uid -> FirestoreHomesReferencesStorage(uid) })
                },
                authRepo = get()) as HomeRepository
    } binds (arrayOf(
            LocalStorageInput::class,
            LocalStorageOutput::class,
            DeviceChannelOutput::class,
            smarthome.raspberry.home_api.data.HomeInfoSource::class
    ))
    single { smarthome.raspberry.authentication.data.AuthRepoImpl() as smarthome.raspberry.authentication_api.AuthRepo }
}

val useCasesModule = module {
    factory { smarthome.raspberry.devices.DevicesUseCase(get()) }
    factory { smarthome.raspberry.authentication.AuthUseCase(get()) }
    factory { smarthome.raspberry.home.HomeUseCase(get()) }
}



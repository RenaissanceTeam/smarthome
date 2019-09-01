package smarthome.raspberry.di

import com.google.gson.typeadapters.RuntimeTypeAdapterFactory
import org.koin.dsl.binds
import org.koin.dsl.module
import smarthome.library.common.BaseController
import smarthome.library.common.DeviceChannelOutput
import smarthome.library.datalibrary.FirestoreHomesReferencesStorage
import smarthome.library.datalibrary.FirestoreSmartHomeStorage
import smarthome.raspberry.arduinodevices.ArduinoDevice
import smarthome.raspberry.arduinodevices.ArduinoDeviceChannel
import smarthome.raspberry.data.*
import smarthome.raspberry.data.local.LocalDevicesStorage
import smarthome.raspberry.data.local.LocalStorageImpl
import smarthome.raspberry.data.local.SharedPreferencesHelper
import smarthome.raspberry.data.local.devices.LocalDevicesStorageImpl
import smarthome.raspberry.data.remote.RemoteStorageImpl
import smarthome.raspberry.domain.AuthRepo
import smarthome.raspberry.domain.HomeRepository
import smarthome.raspberry.domain.usecases.AuthUseCase
import smarthome.raspberry.domain.usecases.ControllersUseCase
import smarthome.raspberry.domain.usecases.DevicesUseCase
import smarthome.raspberry.domain.usecases.HomeUseCase
import smarthome.raspberry.input.InputController
import smarthome.raspberry.input.InputControllerDataSource
import smarthome.raspberry.input.datasource.InputFromSharedDatabase


val dataModule = module {
    factory { SharedPreferencesHelper(get()) }

    val typeAdapter = RuntimeTypeAdapterFactory.of(BaseController::class.java)

    factory { LocalDevicesStorageImpl(typeAdapter) as LocalDevicesStorage }
    factory { LocalStorageImpl(get(), get()) as LocalStorage }
    val deviceChannels = mapOf(
            ArduinoDevice::class.java.simpleName to { output: DeviceChannelOutput ->
                ArduinoDeviceChannel(output)
            }
    )

    single { InputController(get(), get(), get()) }
    factory {
        InputFromSharedDatabase(get(),
                { FirestoreSmartHomeStorage(it) }) as InputControllerDataSource
    }
    single {
        HomeRepositoryImpl(
                localStorageFactory = { input, output ->
                    LocalStorageImpl(get(), get())
                },
                devicesUseCaseFactory = { DevicesUseCase(it) },
                homeUseCaseFactory = { HomeUseCase(it) },
                controllersUseCaseFactory = { ControllersUseCase(it) },
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
            HomeInfoSource::class
    ))
    single { AuthRepoImpl() as AuthRepo }
}

val useCasesModule = module {
    factory { DevicesUseCase(get()) }
    factory { AuthUseCase(get()) }
    factory { HomeUseCase(get()) }
}



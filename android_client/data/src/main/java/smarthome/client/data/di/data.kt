package smarthome.client.data.di

import androidx.room.Room
import com.google.gson.GsonBuilder
import org.koin.dsl.module
import org.koin.experimental.builder.factoryBy
import org.koin.experimental.builder.singleBy
import smarthome.client.data.AppDatabase
import smarthome.client.data.api.auth.LoginCommand
import smarthome.client.data.api.auth.TokenRepo
import smarthome.client.data.api.controllers.ControllersRepo
import smarthome.client.data.api.devices.DevicesRepo
import smarthome.client.data.api.scripts.DependencyDetailsRepo
import smarthome.client.data.api.scripts.ScriptGraphRepo
import smarthome.client.data.api.scripts.ScriptsRepo
import smarthome.client.data.auth.LoginCommandImpl
import smarthome.client.data.auth.TokenRepoImpl
import smarthome.client.data.controllers.ControllersRepoImpl
import smarthome.client.data.devices.DevicesRepoImpl
import smarthome.client.data.devices.mapper.DeviceDetailsToDeviceMapper
import smarthome.client.data.devices.mapper.GeneralDeviceAndControllersInfoToGeneralDeviceInfoMapper
import smarthome.client.data.retrofit.HomeServerUrlHolder
import smarthome.client.data.retrofit.RetrofitFactory
import smarthome.client.data.scripts.DependencyDetailsRepoImpl
import smarthome.client.data.scripts.ScriptGraphRepoImpl
import smarthome.client.data.scripts.ScriptsRepoImpl
import smarthome.client.data.scripts.mapper.ScriptDtoToScriptMapper

val data = module {
    single { GsonBuilder().create() }
    single { RetrofitFactory(urlHolder = get(), getCurrentTokenUseCase = get()) }
    single<AppDatabase> {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            "appdb"
        ).build()
    }
    
    // auth
    factoryBy<LoginCommand, LoginCommandImpl>()
    factory { get<AppDatabase>().homeServerRepo() }
    factory { get<AppDatabase>().userRepo() }
    singleBy<TokenRepo, TokenRepoImpl>()
    single { HomeServerUrlHolder(observeActiveHomeServerUseCase = get()) }
    
    // Devices
    singleBy<DevicesRepo, DevicesRepoImpl>()
    factory { DeviceDetailsToDeviceMapper() }
    factory { GeneralDeviceAndControllersInfoToGeneralDeviceInfoMapper() }
    
    //controllers
    singleBy<ControllersRepo, ControllersRepoImpl>()
    
    //scripts
    singleBy<ScriptsRepo, ScriptsRepoImpl>()
    singleBy<ScriptGraphRepo, ScriptGraphRepoImpl>()
    singleBy<DependencyDetailsRepo, DependencyDetailsRepoImpl>()
    factory { ScriptDtoToScriptMapper() }
}
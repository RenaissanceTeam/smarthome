package smarthome.client.data.di

import android.content.Context
import androidx.room.Room
import com.google.gson.GsonBuilder
import org.koin.dsl.module
import org.koin.experimental.builder.factoryBy
import org.koin.experimental.builder.singleBy
import smarthome.client.data.AppDatabase
import smarthome.client.data.api.auth.LoginCommand
import smarthome.client.data.api.auth.SignUpCommand
import smarthome.client.data.api.auth.TokenRepo
import smarthome.client.data.api.controllers.ControllersRepo
import smarthome.client.data.api.devices.DevicesRepo
import smarthome.client.data.api.location.LocationRepo
import smarthome.client.data.api.notifications.NotificationRepository
import smarthome.client.data.api.scripts.ScriptsRepo
import smarthome.client.data.api.scripts.SetupDependencyRepo
import smarthome.client.data.api.scripts.SetupScriptRepo
import smarthome.client.data.auth.LoginCommandImpl
import smarthome.client.data.auth.SignUpCommandImpl
import smarthome.client.data.auth.TokenRepoImpl
import smarthome.client.data.controllers.ControllersRepoImpl
import smarthome.client.data.devices.DevicesRepoImpl
import smarthome.client.data.devices.mapper.DeviceDetailsToDeviceMapper
import smarthome.client.data.devices.mapper.GeneralDeviceAndControllersInfoToGeneralDeviceInfoMapper
import smarthome.client.data.location.LocationRepoImpl
import smarthome.client.data.notifications.NotificationRepositoryImpl
import smarthome.client.data.retrofit.HomeServerUrlHolder
import smarthome.client.data.retrofit.RetrofitFactory
import smarthome.client.data.scripts.ScriptsRepoImpl
import smarthome.client.data.scripts.SetupDependencyRepoImpl
import smarthome.client.data.scripts.SetupScriptRepoImpl

private val dataInnerModule = module {
    single { GsonBuilder().create() }
    single { RetrofitFactory(urlHolder = get(), getCurrentTokenUseCase = get(), typesConfigurator = get()) }
    single<AppDatabase> {
        Room.databaseBuilder(get(), AppDatabase::class.java, "appdb")
                .fallbackToDestructiveMigration()
                .build()
    }

    // auth
    factoryBy<LoginCommand, LoginCommandImpl>()
    factoryBy<SignUpCommand, SignUpCommandImpl>()
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

    // location
    singleBy<LocationRepo, LocationRepoImpl>()
    factory { get<AppDatabase>().homeGeofenceRepo() }

    //scripts
    singleBy<ScriptsRepo, ScriptsRepoImpl>()
    singleBy<SetupScriptRepo, SetupScriptRepoImpl>()
    singleBy<SetupDependencyRepo, SetupDependencyRepoImpl>()

    // notifications
    single<NotificationRepository> {
        NotificationRepositoryImpl(
                retrofitFactory = get(),
                notificationPreferences = get<Context>().getSharedPreferences("notification_token", Context.MODE_PRIVATE),
                observeAuthenticationStatusUseCase = get()
        )
    }
}

val data = dataInnerModule + typeAdapterModule

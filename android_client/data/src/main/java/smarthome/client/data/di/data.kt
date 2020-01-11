package smarthome.client.data.di

import androidx.room.Room
import com.google.gson.GsonBuilder
import org.koin.dsl.module
import org.koin.experimental.builder.factoryBy
import org.koin.experimental.builder.singleBy
import smarthome.client.data.AppDatabase
import smarthome.client.data.api.auth.LoginCommand
import smarthome.client.data.api.auth.TokenRepo
import smarthome.client.data.api.devices.DevicesRepo
import smarthome.client.data.api.home.HomeRepository
import smarthome.client.data.auth.LoginCommandImpl
import smarthome.client.data.auth.TokenRepoImpl
import smarthome.client.data.devices.DevicesRepoImpl
import smarthome.client.data.home.HomeRepositoryImpl
import smarthome.client.data.retrofit.HomeServerUrlHolder
import smarthome.client.data.retrofit.RetrofitFactory

val data = module {
    single {
        GsonBuilder().create()
    }
    single {
        HomeServerUrlHolder(observeActiveHomeServerUseCase = get())
    }
    factoryBy<LoginCommand, LoginCommandImpl>()
    single { RetrofitFactory(urlHolder = get(), getCurrentTokenUseCase = get()) }
    single<AppDatabase> {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            "appdb"
        ).build()
    }
    
    factory { get<AppDatabase>().homeServerRepo() }
    factory { get<AppDatabase>().userRepo() }
    factoryBy<HomeRepository, HomeRepositoryImpl>()
    singleBy<TokenRepo, TokenRepoImpl>()
    
    singleBy<DevicesRepo, DevicesRepoImpl>()
}
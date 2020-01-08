package smarthome.client.data.di

import com.google.gson.GsonBuilder
import org.koin.dsl.module
import org.koin.experimental.builder.factoryBy
import smarthome.client.data.api.auth.UserRepository
import smarthome.client.data.api.home.HomeRepository
import smarthome.client.data.api.homeserver.HomeServerRepo
import smarthome.client.data.auth.UserRepositoryImpl
import smarthome.client.data.home.HomeRepositoryImpl
import smarthome.client.data.homeserver.HomeServerRepoImpl

val data = module {
    single {
        GsonBuilder().create()
    }
    factoryBy<HomeServerRepo, HomeServerRepoImpl>()
    factoryBy<UserRepository, UserRepositoryImpl>()
    factoryBy<HomeRepository, HomeRepositoryImpl>()
}
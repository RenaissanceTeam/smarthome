package smarthome.client.data.di

import androidx.room.Room
import com.google.gson.GsonBuilder
import org.koin.dsl.module
import org.koin.experimental.builder.factoryBy
import org.koin.experimental.builder.singleBy
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import smarthome.client.data.AppDatabase
import smarthome.client.data.api.auth.LoginCommand
import smarthome.client.data.api.auth.TokenRepo
import smarthome.client.data.api.home.HomeRepository
import smarthome.client.data.auth.LoginApi
import smarthome.client.data.auth.LoginCommandImpl
import smarthome.client.data.auth.TokenRepoImpl
import smarthome.client.data.home.HomeRepositoryImpl
import smarthome.client.data.retrofit.HomeServerUrlHolder

val data = module {
    single {
        GsonBuilder().create()
    }
    single {
        HomeServerUrlHolder(observeActiveHomeServerUseCase = get())
    }
    factoryBy<LoginCommand, LoginCommandImpl>()
    factory {
        Retrofit.Builder()
            .baseUrl(get<HomeServerUrlHolder>().get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    factory { get<Retrofit>().create(LoginApi::class.java) }
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
}
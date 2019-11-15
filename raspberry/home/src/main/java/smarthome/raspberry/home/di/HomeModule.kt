package smarthome.raspberry.home.di

import org.koin.dsl.module
import org.koin.experimental.builder.factoryBy
import org.koin.experimental.builder.singleBy
import smarthome.raspberry.home.api.domain.GenerateUniqueHomeIdUseCase
import smarthome.raspberry.home.api.domain.GetHomeInfoUseCase
import smarthome.raspberry.home.api.domain.LaunchUseCase
import smarthome.raspberry.home.api.presentation.MainFlowLauncher
import smarthome.raspberry.home.data.HomeRepository
import smarthome.raspberry.home.data.HomeRepositoryImpl
import smarthome.raspberry.home.data.storage.LocalStorage
import smarthome.raspberry.home.data.storage.LocalStorageImpl
import smarthome.raspberry.home.data.storage.RemoteStorage
import smarthome.raspberry.home.data.storage.RemoteStorageImpl
import smarthome.raspberry.home.domain.GenerateUniqueHomeIdUseCaseImpl
import smarthome.raspberry.home.domain.GetHomeInfoUseCaseImpl
import smarthome.raspberry.home.domain.LaunchUseCaseImpl
import smarthome.raspberry.home.presentation.MainFlowLauncherImpl

private val domain = module {
    factoryBy<GenerateUniqueHomeIdUseCase, GenerateUniqueHomeIdUseCaseImpl>()
    factoryBy<GetHomeInfoUseCase, GetHomeInfoUseCaseImpl>()
    factoryBy<LaunchUseCase, LaunchUseCaseImpl>()
}

private val data = module {
    singleBy<HomeRepository, HomeRepositoryImpl>()
    factoryBy<LocalStorage, LocalStorageImpl>()
    factoryBy<RemoteStorage, RemoteStorageImpl>()
}

private val presentation = module {
    // todo - how to provide view inside presenter
}

private val flow = module {
    factoryBy<MainFlowLauncher, MainFlowLauncherImpl>()
}


val homeModule = listOf(
        domain,
        data,
        flow,
        presentation
)



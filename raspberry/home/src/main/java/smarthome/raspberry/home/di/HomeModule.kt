package smarthome.raspberry.home.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.experimental.builder.factoryBy
import org.koin.experimental.builder.singleBy
import smarthome.raspberry.home.api.domain.GenerateUniqueHomeIdUseCase
import smarthome.raspberry.home.api.domain.GetHomeIdUseCase
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
import smarthome.raspberry.home.domain.GetHomeIdUseCaseImpl
import smarthome.raspberry.home.domain.GetHomeInfoUseCaseImpl
import smarthome.raspberry.home.domain.LaunchUseCaseImpl
import smarthome.raspberry.home.presentation.MainFlowLauncherImpl
import smarthome.raspberry.home.presentation.main.MainActivity
import smarthome.raspberry.home.presentation.main.MainPresenter
import smarthome.raspberry.home.presentation.main.MainPresenterImpl
import smarthome.raspberry.home.presentation.main.MainView

private val domain = module {
    factoryBy<GenerateUniqueHomeIdUseCase, GenerateUniqueHomeIdUseCaseImpl>()
    factoryBy<GetHomeInfoUseCase, GetHomeInfoUseCaseImpl>()
    factoryBy<LaunchUseCase, LaunchUseCaseImpl>()
    factoryBy<GetHomeIdUseCase, GetHomeIdUseCaseImpl>()
}

private val data = module {
    singleBy<HomeRepository, HomeRepositoryImpl>()
    factoryBy<LocalStorage, LocalStorageImpl>()
    factoryBy<RemoteStorage, RemoteStorageImpl>()
}

private val presentation = module {
    scope(named<MainActivity>()) {
        scoped<MainPresenter> { (view: MainView) ->  MainPresenterImpl(
                getAuthStatusUseCase = get(),
                signInFlowLauncher = get(),
                getHomeInfoUseCase = get(),
                launchUseCase = get(),
                view = view
        ) }
    }
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



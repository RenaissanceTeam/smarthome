package smarthome.raspberry.home.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.experimental.builder.factoryBy
import org.koin.experimental.builder.singleBy
import smarthome.library.datalibrary.api.boundary.HomeIdHolder
import smarthome.raspberry.home.api.domain.*
import smarthome.raspberry.home.api.domain.eventbus.ObserveHomeEventsUseCase
import smarthome.raspberry.home.api.domain.eventbus.ObserveHomeLifecycleUseCase
import smarthome.raspberry.home.api.domain.eventbus.PublishEventUseCase
import smarthome.raspberry.home.api.presentation.MainFlowLauncher
import smarthome.raspberry.home.data.*
import smarthome.raspberry.home.data.storage.LocalStorage
import smarthome.raspberry.home.data.storage.LocalStorageImpl
import smarthome.raspberry.home.data.storage.RemoteStorage
import smarthome.raspberry.home.data.storage.RemoteStorageImpl
import smarthome.raspberry.home.domain.*
import smarthome.raspberry.home.domain.eventbus.ObserveHomeEventsUseCaseImpl
import smarthome.raspberry.home.domain.eventbus.ObserveHomeLifecycleUseCaseImpl
import smarthome.raspberry.home.domain.eventbus.PublishEventUseCaseImpl
import smarthome.raspberry.home.presentation.MainFlowLauncherImpl
import smarthome.raspberry.home.presentation.main.MainActivity
import smarthome.raspberry.home.presentation.main.MainPresenter
import smarthome.raspberry.home.presentation.main.MainPresenterImpl
import smarthome.raspberry.home.presentation.main.MainView

private val domain = module {
    factoryBy<GenerateUniqueHomeIdUseCase, GenerateUniqueHomeIdUseCaseImpl>()
    factoryBy<GetHomeInfoUseCase, GetHomeInfoUseCaseImpl>()
    factoryBy<LaunchUseCase, LaunchUseCaseImpl>()
    factoryBy<ObserveHomeIdUseCase, ObserveHomeIdUseCaseImpl>()
    factoryBy<ClearHomeInfoUseCase, ClearHomeInfoUseCaseImpl>()
    factoryBy<PublishEventUseCase, PublishEventUseCaseImpl>()
    factoryBy<ObserveHomeLifecycleUseCase, ObserveHomeLifecycleUseCaseImpl>()
    factoryBy<ObserveHomeEventsUseCase, ObserveHomeEventsUseCaseImpl>()
    singleBy<HomeStateMachine, HomeStateMachineImpl>()
    singleBy<CreateHomeUseCase, CreateHomeUseCaseImpl>()
    single {
        HomeStateResolver(
            publishEvent = get(),
            getEventsUseCase = get(),
            repo = get(),
            createHomeUseCase = get()
        )
    }
}

private val data = module {
    singleBy<HomeRepository, HomeRepositoryImpl>()
    factoryBy<LocalStorage, LocalStorageImpl>()
    factoryBy<RemoteStorage, RemoteStorageImpl>()
    singleBy<HomeIdHolder, HomeIdHolderImpl>()
    factoryBy<EventBusRepository, EventBusRepositoryImpl>()
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



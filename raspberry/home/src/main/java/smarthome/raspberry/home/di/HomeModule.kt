package smarthome.raspberry.home.di

import org.koin.dsl.module
import org.koin.experimental.builder.factoryBy
import org.koin.experimental.builder.singleBy
import smarthome.raspberry.home.api.domain.LaunchUseCase
import smarthome.raspberry.home.api.domain.eventbus.ObserveHomeEventsUseCase
import smarthome.raspberry.home.api.domain.eventbus.ObserveHomeLifecycleUseCase
import smarthome.raspberry.home.api.domain.eventbus.PublishEventUseCase
import smarthome.raspberry.home.data.EventBusRepository
import smarthome.raspberry.home.data.EventBusRepositoryImpl
import smarthome.raspberry.home.domain.LaunchUseCaseImpl
import smarthome.raspberry.home.domain.eventbus.ObserveHomeEventsUseCaseImpl
import smarthome.raspberry.home.domain.eventbus.ObserveHomeLifecycleUseCaseImpl
import smarthome.raspberry.home.domain.eventbus.PublishEventUseCaseImpl

private val domain = module {
    factoryBy<LaunchUseCase, LaunchUseCaseImpl>()
    factoryBy<PublishEventUseCase, PublishEventUseCaseImpl>()
    factoryBy<ObserveHomeLifecycleUseCase, ObserveHomeLifecycleUseCaseImpl>()
    factoryBy<ObserveHomeEventsUseCase, ObserveHomeEventsUseCaseImpl>()
}

private val data = module {
    singleBy<EventBusRepository, EventBusRepositoryImpl>()
}


val homeModule = listOf(
        domain,
        data
)



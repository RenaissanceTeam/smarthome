package smarthome.client.domain.di

import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.experimental.builder.factoryBy
import smarthome.client.domain.api.homeserver.usecases.ChangeHomeServerUrlUseCase
import smarthome.client.domain.api.homeserver.usecases.GetHomeServerUseCase
import smarthome.client.domain.api.usecase.AuthenticationUseCase
import smarthome.client.domain.api.usecase.CloudMessageUseCase
import smarthome.client.domain.api.usecase.ControllersUseCase
import smarthome.client.domain.api.usecase.DevicesUseCase
import smarthome.client.domain.homeserver.usecases.ChangeHomeServerUrlUseCaseImpl
import smarthome.client.domain.homeserver.usecases.GetHomeServerUseCaseImpl
import smarthome.client.domain.usecases.AuthenticationUseCaseImpl
import smarthome.client.domain.usecases.CloudMessageUseCaseImpl
import smarthome.client.domain.usecases.ControllersUseCaseImpl
import smarthome.client.domain.usecases.DevicesUseCaseImpl

val domain = module {
    factoryBy<GetHomeServerUseCase, GetHomeServerUseCaseImpl>()
    factoryBy<ChangeHomeServerUrlUseCase, ChangeHomeServerUrlUseCaseImpl>()
    factoryBy<AuthenticationUseCase, AuthenticationUseCaseImpl>()
    factoryBy<CloudMessageUseCase, CloudMessageUseCaseImpl>()
    factoryBy<ControllersUseCase, ControllersUseCaseImpl>()
    factoryBy<DevicesUseCase, DevicesUseCaseImpl>()
}
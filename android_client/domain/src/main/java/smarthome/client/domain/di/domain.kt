package smarthome.client.domain.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.experimental.builder.factoryBy
import org.koin.experimental.builder.singleBy
import smarthome.client.domain.api.auth.usecases.*
import smarthome.client.domain.api.devices.usecase.GetDeviceUseCase
import smarthome.client.domain.api.devices.usecase.GetGeneralDevicesInfo
import smarthome.client.domain.api.homeserver.usecases.ChangeHomeServerUrlUseCase
import smarthome.client.domain.api.homeserver.usecases.ObserveActiveHomeServerUseCase
import smarthome.client.domain.api.main.BooleanState
import smarthome.client.domain.api.main.StateMachine
import smarthome.client.domain.api.usecase.CloudMessageUseCase
import smarthome.client.domain.api.usecase.ControllersUseCase
import smarthome.client.domain.api.usecase.DevicesUseCase
import smarthome.client.domain.auth.usecases.*
import smarthome.client.domain.devices.usecase.GetDeviceUseCaseImpl
import smarthome.client.domain.devices.usecase.GetGeneralDevicesInfoImpl
import smarthome.client.domain.homeserver.usecases.ChangeHomeServerUrlUseCaseImpl
import smarthome.client.domain.homeserver.usecases.ObserveActiveHomeServerUseCaseImpl
import smarthome.client.domain.main.StateMachineImpl
import smarthome.client.domain.usecases.CloudMessageUseCaseImpl
import smarthome.client.domain.usecases.ControllersUseCaseImpl
import smarthome.client.domain.usecases.DevicesUseCaseImpl

val domain = module {
    // homeserver
    factoryBy<ObserveActiveHomeServerUseCase, ObserveActiveHomeServerUseCaseImpl>()
    factoryBy<ChangeHomeServerUrlUseCase, ChangeHomeServerUrlUseCaseImpl>()
    
    // auth
    factoryBy<ObserveAuthenticationStatusUseCase,ObserveAuthenticationStatusUseCaseImpl>()
    factoryBy<ObserveCurrentTokenUseCase, ObserveCurrentTokenUseCaseImpl>()
    factoryBy<ObserveCurrentUserUseCase, ObserveCurrentUserUseCaseImpl>()
    factoryBy<LoginUseCase, LoginUseCaseImpl>()
    factoryBy<SaveNewTokenUseCase, SaveNewTokenUseCaseImpl>()
    factoryBy<GetCurrentTokenUseCase, GetCurrentTokenUseCaseImpl>()
    
    // devices
    factoryBy<GetGeneralDevicesInfo, GetGeneralDevicesInfoImpl>()
    factoryBy<GetDeviceUseCase, GetDeviceUseCaseImpl>()
    
    factoryBy<CloudMessageUseCase, CloudMessageUseCaseImpl>()
    factoryBy<ControllersUseCase, ControllersUseCaseImpl>()
    factoryBy<DevicesUseCase, DevicesUseCaseImpl>()
    
    single(named("login")) {
        BooleanState()
    }
    single(named("homeServer")) {
        BooleanState()
    }
    
    single<StateMachine> {
        StateMachineImpl(
            loginState = get<BooleanState>(named("login")),
            homeServerState = get<BooleanState>(named("homeServer"))
        )
    }
    
}
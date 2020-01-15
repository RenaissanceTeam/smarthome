package smarthome.client.domain.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.experimental.builder.factoryBy
import smarthome.client.domain.api.auth.usecases.*
import smarthome.client.domain.api.conrollers.usecases.GetControllerUseCase
import smarthome.client.domain.api.conrollers.usecases.ObserveControllerUseCase
import smarthome.client.domain.api.conrollers.usecases.ReadControllerUseCase
import smarthome.client.domain.api.conrollers.usecases.WriteStateToControllerUseCase
import smarthome.client.domain.api.devices.usecase.*
import smarthome.client.domain.api.homeserver.usecases.ChangeHomeServerUrlUseCase
import smarthome.client.domain.api.homeserver.usecases.ObserveActiveHomeServerUseCase
import smarthome.client.domain.api.main.BooleanState
import smarthome.client.domain.api.main.StateMachine
import smarthome.client.domain.api.usecase.CloudMessageUseCase
import smarthome.client.domain.api.usecase.ControllersUseCase
import smarthome.client.domain.api.usecase.DevicesUseCase
import smarthome.client.domain.auth.usecases.*
import smarthome.client.domain.conrollers.usecases.GetControllerUseCaseImpl
import smarthome.client.domain.conrollers.usecases.ObserveControllerUseCaseImpl
import smarthome.client.domain.conrollers.usecases.ReadControllerUseCaseImpl
import smarthome.client.domain.conrollers.usecases.WriteStateToControllerUseCaseImpl
import smarthome.client.domain.devices.usecase.*
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
    factoryBy<GetGeneralDevicesInfo, GetAddedDevicesInfoImpl>()
    factoryBy<GetDeviceUseCase, GetDeviceUseCaseImpl>()
    factoryBy<GetPendingDevicesUseCase, GetPendingDevicesUseCaseImpl>()
    factoryBy<DeclinePendingDeviceUseCase, DeclinePendingDeviceUseCaseImpl>()
    factoryBy<AcceptPendingDeviceUseCase, AcceptPendingDeviceUseCaseImpl>()
    
    // controllers
    factoryBy<ObserveControllerUseCase, ObserveControllerUseCaseImpl>()
    factoryBy<GetControllerUseCase, GetControllerUseCaseImpl>()
    factoryBy<ReadControllerUseCase, ReadControllerUseCaseImpl>()
    factoryBy<WriteStateToControllerUseCase, WriteStateToControllerUseCaseImpl>()
    
    
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
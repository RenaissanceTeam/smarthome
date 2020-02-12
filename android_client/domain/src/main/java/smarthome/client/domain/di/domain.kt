package smarthome.client.domain.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.experimental.builder.factoryBy
import smarthome.client.domain.api.auth.usecases.*
import smarthome.client.domain.api.conrollers.usecases.*
import smarthome.client.domain.api.devices.usecase.*
import smarthome.client.domain.api.homeserver.usecases.ChangeHomeServerUrlUseCase
import smarthome.client.domain.api.homeserver.usecases.ObserveActiveHomeServerUseCase
import smarthome.client.domain.api.main.BooleanState
import smarthome.client.domain.api.main.StateMachine
import smarthome.client.domain.api.scripts.usecases.*
import smarthome.client.domain.api.scripts.usecases.dependency.GetSetupDependencyUseCase
import smarthome.client.domain.api.scripts.usecases.dependency.StartSetupDependencyUseCase
import smarthome.client.domain.api.usecase.CloudMessageUseCase
import smarthome.client.domain.auth.usecases.*
import smarthome.client.domain.conrollers.usecases.*
import smarthome.client.domain.devices.usecase.*
import smarthome.client.domain.homeserver.usecases.ChangeHomeServerUrlUseCaseImpl
import smarthome.client.domain.homeserver.usecases.ObserveActiveHomeServerUseCaseImpl
import smarthome.client.domain.main.StateMachineImpl
import smarthome.client.domain.scripts.usecases.*
import smarthome.client.domain.scripts.usecases.dependency.GetSetupDependencyUseCaseImpl
import smarthome.client.domain.scripts.usecases.dependency.StartSetupDependencyUseCaseImpl
import smarthome.client.domain.usecases.CloudMessageUseCaseImpl

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
    factoryBy<FetchControllerUseCase, FetchControllerUseCaseImpl>()
    factoryBy<ReadControllerUseCase, ReadControllerUseCaseImpl>()
    factoryBy<WriteStateToControllerUseCase, WriteStateToControllerUseCaseImpl>()
    factoryBy<PipelineControllerToStorageUseCase, PipelineControllerToStorageUseCaseImpl>()
    factoryBy<GetControllerUseCase, GetControllerUseCaseImpl>()
    
    //scripts
    factoryBy<FetchScriptsUseCase, FetchScriptsUseCaseImpl>()
    factoryBy<AddBlockToScriptGraphUseCase, AddBlockToScriptGraphUseCaseImpl>()
    factoryBy<MoveBlockUseCase, MoveBlockUseCaseImpl>()
    factoryBy<RemoveBlockUseCase, RemoveBlockUseCaseImpl>()
    factoryBy<AddDependencyUseCase, AddDependencyUseCaseImpl>()
    factoryBy<ObserveBlocksUseCase, ObserveBlocksUseCaseImpl>()
    factoryBy<ObserveDependenciesUseCase, ObserveDependenciesUseCaseImpl>()
    factoryBy<CheckIfDependencyPossibleUseCase, CheckIfDependencyPossibleUseCaseImpl>()
    factoryBy<AddControllerBlockUseCase, AddControllerBlockUseCaseImpl>()
    factoryBy<RemoveDependencyUseCase, RemoveDependencyUseCaseImpl>()
    factoryBy<FetchDependencyDetailsUseCase, FetchDependencyDetailsUseCaseImpl>()
    factoryBy<ObserveDependencyDetailsUseCase, ObserveDependencyDetailsUseCaseImpl>()
    factoryBy<CreateEmptyConditionsForBlockUseCase, CreateEmptyConditionsForBlockUseCaseImpl>()
    factoryBy<GetDependencyUseCase, GetDependencyUseCaseImpl>()
    factoryBy<CreateEmptyActionForBlockUseCase, CreateEmptyActionForBlockUseCaseImpl>()
    factoryBy<GetDependencyDetailsUseCase, GetDependencyDetailsUseCaseImpl>()
    factoryBy<AddDependencyDetailsUseCase, AddDependencyDetailsUseCaseImpl>()
    factoryBy<UpdateDependencyDetailsUseCase, UpdateDependencyDetailsUseCaseImpl>()
    factoryBy<GetSetupDependencyUseCase, GetSetupDependencyUseCaseImpl>()
    factoryBy<StartSetupDependencyUseCase, StartSetupDependencyUseCaseImpl>()
    
    factoryBy<CloudMessageUseCase, CloudMessageUseCaseImpl>()
    
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
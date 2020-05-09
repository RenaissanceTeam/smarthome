package smarthome.client.domain.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.experimental.builder.factoryBy
import smarthome.client.domain.api.auth.usecases.*
import smarthome.client.domain.api.conrollers.usecases.*
import smarthome.client.domain.api.devices.usecase.*
import smarthome.client.domain.api.homeserver.usecases.ChangeHomeServerUrlUseCase
import smarthome.client.domain.api.homeserver.usecases.ObserveActiveHomeServerUseCase
import smarthome.client.domain.api.homeserver.usecases.ObserveRecentServersUseCase
import smarthome.client.domain.api.location.usecases.GetGeofenceUseCase
import smarthome.client.domain.api.location.usecases.SetupGeofenceUseCase
import smarthome.client.domain.api.notifications.SaveNotificationTokenUseCase
import smarthome.client.domain.api.scripts.resolver.ActionFromBlockResolver
import smarthome.client.domain.api.scripts.resolver.ConditionFromBlockResolver
import smarthome.client.domain.api.scripts.usecases.GetScriptByIdUseCase
import smarthome.client.domain.api.scripts.usecases.GetScriptsOverviewUseCase
import smarthome.client.domain.api.scripts.usecases.dependency.*
import smarthome.client.domain.api.scripts.usecases.setup.*
import smarthome.client.domain.auth.usecases.*
import smarthome.client.domain.conrollers.usecases.*
import smarthome.client.domain.devices.usecase.*
import smarthome.client.domain.homeserver.usecases.ChangeHomeServerUrlUseCaseImpl
import smarthome.client.domain.homeserver.usecases.ObserveActiveHomeServerUseCaseImpl
import smarthome.client.domain.homeserver.usecases.ObserveRecentServersUseCaseImpl
import smarthome.client.domain.location.usecases.GetGeofenceUseCaseImpl
import smarthome.client.domain.location.usecases.SetupGeofenceUseCaseImpl
import smarthome.client.domain.notifications.SaveNotificationTokenUseCaseImpl
import smarthome.client.domain.scripts.blocks.location.LocationConditionFromBlockResolver
import smarthome.client.domain.scripts.blocks.notification.NotificationActionFromBlockResolver
import smarthome.client.domain.scripts.blocks.time.TimeConditionFromBlockResolver
import smarthome.client.domain.scripts.usecases.GetScriptByIdUseCaseImpl
import smarthome.client.domain.scripts.usecases.GetScriptsOverviewUseCaseImpl
import smarthome.client.domain.scripts.usecases.dependency.*
import smarthome.client.domain.scripts.usecases.setup.*

val domain = module {
    // homeserver
    factoryBy<ObserveActiveHomeServerUseCase, ObserveActiveHomeServerUseCaseImpl>()
    factoryBy<ChangeHomeServerUrlUseCase, ChangeHomeServerUrlUseCaseImpl>()
    factoryBy<ObserveRecentServersUseCase, ObserveRecentServersUseCaseImpl>()

    // auth
    factoryBy<ObserveAuthenticationStatusUseCase, ObserveAuthenticationStatusUseCaseImpl>()
    factoryBy<ObserveCurrentTokenUseCase, ObserveCurrentTokenUseCaseImpl>()
    factoryBy<ObserveCurrentUserUseCase, ObserveCurrentUserUseCaseImpl>()
    factoryBy<LoginUseCase, LoginUseCaseImpl>()
    factoryBy<SaveNewTokenUseCase, SaveNewTokenUseCaseImpl>()
    factoryBy<GetCurrentTokenUseCase, GetCurrentTokenUseCaseImpl>()
    factoryBy<LogoutUseCase, LogoutUseCaseImpl>()
    factoryBy<SignUpUseCase, SignUpUseCaseImpl>()

    // devices
    factoryBy<GetGeneralDevicesInfo, GetAddedDevicesInfoImpl>()
    factoryBy<GetDeviceUseCase, GetDeviceUseCaseImpl>()
    factoryBy<GetPendingDevicesUseCase, GetPendingDevicesUseCaseImpl>()
    factoryBy<DeclinePendingDeviceUseCase, DeclinePendingDeviceUseCaseImpl>()
    factoryBy<AcceptPendingDeviceUseCase, AcceptPendingDeviceUseCaseImpl>()
    factoryBy<UpdateDeviceNameUseCase, UpdateDeviceNameUseCaseImpl>()
    factoryBy<UpdateDeviceDescriptionUseCase, UpdateDeviceDescriptionUseCaseImpl>()

    // controllers
    factoryBy<ObserveControllerUseCase, ObserveControllerUseCaseImpl>()
    factoryBy<FetchControllerUseCase, FetchControllerUseCaseImpl>()
    factoryBy<ReadControllerUseCase, ReadControllerUseCaseImpl>()
    factoryBy<WriteStateToControllerUseCase, WriteStateToControllerUseCaseImpl>()
    factoryBy<PipelineControllerToStorageUseCase, PipelineControllerToStorageUseCaseImpl>()
    factoryBy<GetControllerUseCase, GetControllerUseCaseImpl>()
    factoryBy<UpdateControllerNameUseCase, UpdateControllerNameUseCaseImpl>()

    // location
    factoryBy<SetupGeofenceUseCase, SetupGeofenceUseCaseImpl>()
    factoryBy<GetGeofenceUseCase, GetGeofenceUseCaseImpl>()

    //scripts
    factoryBy<MoveBlockUseCase, MoveBlockUseCaseImpl>()
    factoryBy<RemoveBlockUseCase, RemoveBlockUseCaseImpl>()
    factoryBy<AddDependencyUseCase, AddDependencyUseCaseImpl>()
    factoryBy<ObserveBlocksUseCase, ObserveBlocksUseCaseImpl>()
    factoryBy<ObserveDependenciesUseCase, ObserveDependenciesUseCaseImpl>()
    factoryBy<CheckIfDependencyPossibleUseCase, CheckIfDependencyPossibleUseCaseImpl>()
    factoryBy<AddBlockUseCase, AddBlockUseCaseImpl>()
    factoryBy<RemoveDependencyUseCase, RemoveDependencyUseCaseImpl>()
    factoryBy<CreateEmptyConditionsForDependencyUseCase, CreateEmptyConditionsForDependencyUseCaseImpl>()
    factoryBy<GetDependencyUseCase, GetDependencyUseCaseImpl>()
    factoryBy<CreateEmptyActionForDependencyUseCase, CreateEmptyActionForDependencyUseCaseImpl>()
    factoryBy<GetSetupDependencyUseCase, GetSetupDependencyUseCaseImpl>()
    factoryBy<StartSetupDependencyUseCase, StartSetupDependencyUseCaseImpl>()
    factoryBy<ChangeSetupDependencyConditionUseCase, ChangeSetupDependencyConditionUseCaseImpl>()
    factoryBy<ChangeSetupDependencyActionUseCase, ChangeSetupDependencyActionUseCaseImpl>()
    factoryBy<ObserveSetupDependencyUseCase, ObserveSetupDependencyUseCaseImpl>()
    factoryBy<GetBlockNameUseCase, GetBlockNameUseCaseImpl>()
    factoryBy<GetBlockUseCase, GetBlockUseCaseImpl>()
    factoryBy<AddConditionToSetupDependencyUseCase, AddConditionToSetupDependencyUseCaseImpl>()
    factoryBy<RemoveConditionsFromSetupDependencyUseCase, RemoveConditionsFromSetupDependencyUseCaseImpl>()
    factoryBy<UpdateSetupDependencyUseCase, UpdateSetupDependencyUseCaseImpl>()
    factoryBy<SaveSetupDependencyUseCase, SaveSetupDependencyUseCaseImpl>()
    factoryBy<StartSetupScriptUseCase, StartSetupScriptUseCaseImpl>()
    factoryBy<GetScriptByIdUseCase, GetScriptByIdUseCaseImpl>()
    factoryBy<CancelSetupScriptUseCase, CancelSetupScriptUseCaseImpl>()
    factoryBy<SaveSetupScriptUseCase, SaveSetupScriptUseCaseImpl>()
    factoryBy<IsSetupInProgressUseCase, IsSetupInProgressUseCaseImpl>()
    factoryBy<UpdateScriptInfoUseCase, UpdateScriptInfoUseCaseImpl>()
    factoryBy<ObserveSetupScriptUseCase, ObserveSetupScriptUseCaseImpl>()
    factoryBy<GetSetupScriptUseCase, GetSetupScriptUseCaseImpl>()
    factoryBy<GetScriptsOverviewUseCase, GetScriptsOverviewUseCaseImpl>()
    factoryBy<CheckIfCanStartDependencyFromUseCase, CheckIfCanStartDependencyFromUseCaseImpl>()
    factory<ActionFromBlockResolver>(named<NotificationActionFromBlockResolver>()) { NotificationActionFromBlockResolver() }
    factory<ConditionFromBlockResolver>(named<TimeConditionFromBlockResolver>()) { TimeConditionFromBlockResolver() }
    factory<ConditionFromBlockResolver>(named<LocationConditionFromBlockResolver>()) { LocationConditionFromBlockResolver() }

    // notifications
    factoryBy<SaveNotificationTokenUseCase, SaveNotificationTokenUseCaseImpl>()
}
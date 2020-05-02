package smarthome.client.presentation.di

import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.experimental.builder.factoryBy
import org.koin.experimental.builder.singleBy
import smarthome.client.domain.api.scripts.resolver.BlockNameResolver
import smarthome.client.domain.api.scripts.usecases.setup.CreateEmptyActionForDependencyUseCase
import smarthome.client.domain.api.scripts.usecases.setup.CreateEmptyConditionsForDependencyUseCase
import smarthome.client.entity.script.dependency.action.Action
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.presentation.ACTION_CONTAINER_CONTROLLER
import smarthome.client.presentation.ACTION_CONTAINER_VIEWMODEL
import smarthome.client.presentation.CONDITION_CONTAINER_CONTROLLER
import smarthome.client.presentation.CONDITION_CONTAINER_VIEWMODEL
import smarthome.client.presentation.controllers.controllerdetail.statechanger.StateChangerFactory
import smarthome.client.presentation.main.toolbar.ToolbarController
import smarthome.client.presentation.main.toolbar.ToolbarControllerImpl
import smarthome.client.presentation.main.toolbar.ToolbarHolder
import smarthome.client.presentation.main.toolbar.ToolbarSetter
import smarthome.client.presentation.scripts.resolver.ActionModelResolver
import smarthome.client.presentation.scripts.resolver.ConditionModelResolver
import smarthome.client.presentation.scripts.setup.SetupScriptViewModel
import smarthome.client.presentation.scripts.setup.controllers.ControllersHubViewModel
import smarthome.client.presentation.scripts.setup.dependency.ContainersViewModel
import smarthome.client.presentation.scripts.setup.dependency.action.notification.SendNotificationActionModelResolver
import smarthome.client.presentation.scripts.setup.dependency.container.ContainerId
import smarthome.client.presentation.scripts.setup.dependency.container.ContainersController
import smarthome.client.presentation.scripts.setup.graph.blockviews.controller.ControllerBlockFactory
import smarthome.client.presentation.scripts.setup.graph.blockviews.dependency.MovingDependency
import smarthome.client.presentation.scripts.setup.graph.blockviews.factory.*
import smarthome.client.presentation.scripts.setup.graph.blockviews.notifications.NotificationBlockFactory
import smarthome.client.presentation.scripts.setup.graph.blockviews.notifications.NotificationBlockNameResolver
import smarthome.client.presentation.scripts.setup.graph.eventhandler.DependencyEventsHandler
import smarthome.client.presentation.scripts.setup.graph.eventhandler.DependencyEventsHandlerImpl
import smarthome.client.presentation.scripts.setup.graph.eventhandler.DragBlockEventsHandler
import smarthome.client.presentation.scripts.setup.graph.eventhandler.DragBlockEventsHandlerImpl
import smarthome.client.presentation.scripts.setup.graph.events.GraphEventBus
import smarthome.client.presentation.scripts.setup.graph.events.GraphEventBusImpl
import smarthome.client.presentation.scripts.setup.graph.mapper.BlockToNewGraphBlockStateMapper
import smarthome.client.presentation.scripts.setup.graph.mapper.DependencyToDependencyStateMapper
import smarthome.client.presentation.scripts.setup.graph.view.GraphViewModel
import smarthome.client.presentation.util.drag.DraggableHostHolder
import smarthome.client.presentation.util.drag.DraggableHostHolderImpl

val presentation = module {

    // controllers
    factory { StateChangerFactory() }

    // toolbar
    single { ToolbarHolder() }
    factoryBy<ToolbarController, ToolbarControllerImpl>()
    factory { (owner: LifecycleOwner, toolbar: Toolbar) -> ToolbarSetter(owner, toolbar, get()) }

    // scripts
    viewModel { SetupScriptViewModel() }
    singleBy<GraphEventBus, GraphEventBusImpl>()
    factoryBy<GraphBlockFactoryResolver, GraphBlockFactoryResolverImpl>()
    factory { BlockToNewGraphBlockStateMapper() }
    factory { DependencyToDependencyStateMapper() }
    factoryBy<DragBlockEventsHandler, DragBlockEventsHandlerImpl>()

    factory<DependencyEventsHandler> { (movingDependency: MutableLiveData<MovingDependency>) ->
        DependencyEventsHandlerImpl(movingDependency = movingDependency)
    }
    factory(named(CONDITION_CONTAINER_CONTROLLER)) { (onScroll: (ContainerId, Condition) -> Unit) ->
        ContainersController(get<ConditionModelResolver>(), onScroll)
    }
    factory(named(ACTION_CONTAINER_CONTROLLER)) { (onScroll: (ContainerId, Action) -> Unit) ->
        ContainersController(get<ActionModelResolver>(), onScroll)
    }
    factory(named(CONDITION_CONTAINER_VIEWMODEL)) { ContainersViewModel(get<CreateEmptyConditionsForDependencyUseCase>()::execute) }
    factory(named(ACTION_CONTAINER_VIEWMODEL)) { ContainersViewModel(get<CreateEmptyActionForDependencyUseCase>()::execute) }

    factory<GraphBlockFactory>(named(CONTROLLER_FACTORY)) { ControllerBlockFactory() }
    factory<GraphBlockFactory>(named(NOTIFICATION_FACTORY)) { NotificationBlockFactory() }

    factory<ActionModelResolver>(named<SendNotificationActionModelResolver>()) { SendNotificationActionModelResolver(get()) }
    factory<BlockNameResolver>(named<NotificationBlockNameResolver>()) { NotificationBlockNameResolver() }

    singleBy<DraggableHostHolder, DraggableHostHolderImpl>()

    scope<String> {
        scoped { SetupScriptViewModel() }
        scoped { ControllersHubViewModel() }
        scoped { GraphViewModel() }
    }
}
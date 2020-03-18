package smarthome.client.presentation.di

import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.experimental.builder.factoryBy
import org.koin.experimental.builder.singleBy
import smarthome.client.domain.api.scripts.usecases.CreateEmptyActionForDependencyUseCase
import smarthome.client.domain.api.scripts.usecases.CreateEmptyConditionsForDependencyUseCase
import smarthome.client.entity.script.dependency.action.Action
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.presentation.ACTION_CONTAINER_CONTROLLER
import smarthome.client.presentation.ACTION_CONTAINER_VIEWMODEL
import smarthome.client.presentation.CONDITION_CONTAINER_CONTROLLER
import smarthome.client.presentation.CONDITION_CONTAINER_VIEWMODEL
import smarthome.client.presentation.controllers.controllerdetail.statechanger.OnOffStateChanger
import smarthome.client.presentation.controllers.controllerdetail.statechanger.ReadStateChanger
import smarthome.client.presentation.controllers.controllerdetail.statechanger.StateChangerFactory
import smarthome.client.presentation.main.toolbar.ToolbarController
import smarthome.client.presentation.main.toolbar.ToolbarControllerImpl
import smarthome.client.presentation.main.toolbar.ToolbarHolder
import smarthome.client.presentation.main.toolbar.ToolbarSetter
import smarthome.client.presentation.scripts.addition.SetupScriptViewModel
import smarthome.client.presentation.scripts.addition.controllers.ControllersHubViewModel
import smarthome.client.presentation.scripts.addition.dependency.ContainersViewModel
import smarthome.client.presentation.scripts.addition.dependency.container.ContainerId
import smarthome.client.presentation.scripts.addition.dependency.container.ContainersController
import smarthome.client.presentation.scripts.addition.graph.ScriptGraphFragment
import smarthome.client.presentation.scripts.addition.graph.blockviews.dependency.MovingDependency
import smarthome.client.presentation.scripts.addition.graph.blockviews.factory.*
import smarthome.client.presentation.scripts.addition.graph.blockviews.state.BlockState
import smarthome.client.presentation.scripts.addition.graph.eventhandler.DependencyEventsHandler
import smarthome.client.presentation.scripts.addition.graph.eventhandler.DependencyEventsHandlerImpl
import smarthome.client.presentation.scripts.addition.graph.eventhandler.DragBlockEventsHandler
import smarthome.client.presentation.scripts.addition.graph.eventhandler.DragBlockEventsHandlerImpl
import smarthome.client.presentation.scripts.addition.graph.events.GraphEventBus
import smarthome.client.presentation.scripts.addition.graph.events.GraphEventBusImpl
import smarthome.client.presentation.scripts.addition.graph.helper.AddBlockHelper
import smarthome.client.presentation.scripts.addition.graph.mapper.BlockToNewGraphBlockStateMapper
import smarthome.client.presentation.scripts.addition.graph.mapper.DependencyToDependencyStateMapper
import smarthome.client.presentation.scripts.addition.graph.view.GraphViewModel
import smarthome.client.presentation.scripts.resolver.ActionModelResolver
import smarthome.client.presentation.scripts.resolver.ConditionModelResolver

val presentation = module {
    
    // controllers
    factory { StateChangerFactory() }
    factory(named("dht")) { (controllerId: Long) ->
        ReadStateChanger(id = controllerId, readControllerUseCase = get())
    }
    
    factory(named("onoff")) { (controllerId: Long) ->
        OnOffStateChanger(id = controllerId, writeStateToControllerUseCase = get(), observeControllerUseCase = get())
    }
    
    
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
    factory<DragBlockEventsHandler> { (blocks: MutableLiveData<List<BlockState>>) ->
        DragBlockEventsHandlerImpl(
            blocks = blocks,
            addBlockHelper = get(),
            blockToNewGraphBlockStateMapper = get(),
            moveBlockUseCase = get(),
            removeBlockUseCase = get()
        )
    }
    
    factory<DependencyEventsHandler> { (movingDependency: MutableLiveData<MovingDependency>) ->
        DependencyEventsHandlerImpl(movingDependency = movingDependency)
    }
    factory { AddBlockHelper(get()) }
    factory(named(CONDITION_CONTAINER_CONTROLLER)) { (onScroll: (ContainerId, Condition) -> Unit) ->
        ContainersController(get<ConditionModelResolver>(), onScroll)
    }
    factory(named(ACTION_CONTAINER_CONTROLLER)) { (onScroll: (ContainerId, Action) -> Unit) ->
        ContainersController(get<ActionModelResolver>(), onScroll)
    }
    factory(named(CONDITION_CONTAINER_VIEWMODEL)) { ContainersViewModel(get<CreateEmptyConditionsForDependencyUseCase>()::execute) }
    factory(named(ACTION_CONTAINER_VIEWMODEL)) { ContainersViewModel(get<CreateEmptyActionForDependencyUseCase>()::execute) }
    
    factory<GraphBlockFactory>(named(CONTROLLER_FACTORY)) { ControllerBlockFactoryImpl() }
    
    scope<ScriptGraphFragment> {
        scoped { ControllersHubViewModel() }
        scoped { GraphViewModel() }
    }
}
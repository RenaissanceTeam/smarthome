package smarthome.client.presentation.di

import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LifecycleOwner
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.experimental.builder.factoryBy
import org.koin.experimental.builder.singleBy
import smarthome.client.presentation.controllers.controllerdetail.statechanger.OnOffStateChanger
import smarthome.client.presentation.controllers.controllerdetail.statechanger.ReadStateChanger
import smarthome.client.presentation.controllers.controllerdetail.statechanger.StateChangerFactory
import smarthome.client.presentation.main.toolbar.ToolbarController
import smarthome.client.presentation.main.toolbar.ToolbarControllerImpl
import smarthome.client.presentation.main.toolbar.ToolbarHolder
import smarthome.client.presentation.main.toolbar.ToolbarSetter
import smarthome.client.presentation.scripts.addition.AddScriptViewModel
import smarthome.client.presentation.scripts.addition.graph.*
import smarthome.client.presentation.scripts.addition.graph.blockviews.factory.*
import smarthome.client.presentation.scripts.addition.graph.blockviews.state.GraphBlockResolver
import smarthome.client.presentation.scripts.addition.graph.blockviews.state.GraphBlockResolverImpl
import smarthome.client.presentation.scripts.addition.graph.events.GraphEventBus
import smarthome.client.presentation.scripts.addition.graph.events.GraphEventBusImpl

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
    viewModel { AddScriptViewModel() }
    singleBy<GraphEventBus, GraphEventBusImpl>()
    factoryBy<GraphBlockFactoryResolver, GraphBlockFactoryResolverImpl>()
    factoryBy<GraphBlockResolver, GraphBlockResolverImpl>()
    factory { BlockToNewGraphBlockStateMapper() }
    factoryBy<DragBlockEventsHandler, DragBlockEventsHandlerImpl>()
    factoryBy<DependencyEventsHandler, DependencyEventsHandlerImpl>()
    factory { AddBlockHelper(get()) }
    factory { AddGraphBlockStateHelper() }
    
    factory<GraphBlockFactory>(named(CONTROLLER_FACTORY)) { ControllerBlockFactoryImpl() }
}
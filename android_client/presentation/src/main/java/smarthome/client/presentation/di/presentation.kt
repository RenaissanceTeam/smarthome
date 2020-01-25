package smarthome.client.presentation.di

import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LifecycleOwner
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.experimental.builder.factory
import org.koin.experimental.builder.factoryBy
import smarthome.client.entity.Controller
import smarthome.client.presentation.controllers.controllerdetail.statechanger.OnOffStateChanger
import smarthome.client.presentation.controllers.controllerdetail.statechanger.ReadStateChanger
import smarthome.client.presentation.controllers.controllerdetail.statechanger.StateChangerFactory
import smarthome.client.presentation.main.toolbar.ToolbarController
import smarthome.client.presentation.main.toolbar.ToolbarControllerImpl
import smarthome.client.presentation.main.toolbar.ToolbarHolder
import smarthome.client.presentation.main.toolbar.ToolbarSetter
import smarthome.client.presentation.scripts.addition.AddScriptViewModel
import smarthome.client.presentation.scripts.addition.graph.ControllerBlockFactoryImpl
import smarthome.client.presentation.scripts.addition.graph.GraphBlockFactory
import smarthome.client.presentation.scripts.addition.graph.GraphBlockFactoryResolver
import smarthome.client.presentation.scripts.addition.graph.GraphBlockFactoryResolverImpl

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
    factoryBy<GraphBlockFactoryResolver, GraphBlockFactoryResolverImpl>()
    factory<GraphBlockFactory>(named("controller")) { ControllerBlockFactoryImpl() }
}
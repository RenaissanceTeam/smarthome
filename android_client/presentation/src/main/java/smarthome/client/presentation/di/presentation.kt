package smarthome.client.presentation.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import smarthome.client.entity.Controller
import smarthome.client.presentation.controllers.controllerdetail.statechanger.OnOffStateChanger
import smarthome.client.presentation.controllers.controllerdetail.statechanger.ReadStateChanger
import smarthome.client.presentation.controllers.controllerdetail.statechanger.StateChangerFactory

val presentation = module {
    
    // controllers
    factory { StateChangerFactory() }
    factory(named("dht")) { (controller: Controller) ->
        ReadStateChanger(controller = controller, readControllerUseCase = get())
    }
    
    factory(named("onoff")) { (controllerId: Long) ->
        OnOffStateChanger(id = controllerId, writeStateToControllerUseCase = get(), observeControllerUseCase = get())
    }
    
}
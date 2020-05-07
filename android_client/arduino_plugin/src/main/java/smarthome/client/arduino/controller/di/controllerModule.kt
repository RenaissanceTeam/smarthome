package smarthome.client.arduino.controller.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import smarthome.client.arduino.controller.presentation.OnOffStateChanger
import smarthome.client.arduino.scripts.entity.*
import smarthome.client.presentation.controllers.controllerdetail.statechanger.ControllerStateChanger
import smarthome.client.presentation.controllers.controllerdetail.statechanger.ReadStateChanger


val controllerModule = module {
    factory<ControllerStateChanger>(named(temperature11)) { (controllerId: Long) ->
        ReadStateChanger(id = controllerId, readControllerUseCase = get())
    }

    factory<ControllerStateChanger>(named(temperature22)) { (controllerId: Long) ->
        ReadStateChanger(id = controllerId, readControllerUseCase = get())
    }

    factory<ControllerStateChanger>(named(humidity11)) { (controllerId: Long) ->
        ReadStateChanger(id = controllerId, readControllerUseCase = get())
    }

    factory<ControllerStateChanger>(named(humidity22)) { (controllerId: Long) ->
        ReadStateChanger(id = controllerId, readControllerUseCase = get())
    }

    factory<ControllerStateChanger>(named(analog)) { (controllerId: Long) ->
        ReadStateChanger(id = controllerId, readControllerUseCase = get())
    }

    factory<ControllerStateChanger>(named(digital)) { (controllerId: Long) ->
        ReadStateChanger(id = controllerId, readControllerUseCase = get())
    }

    factory<ControllerStateChanger>(named(onoff)) { (controllerId: Long) ->
        OnOffStateChanger(
                id = controllerId,
                writeStateToControllerUseCase = get(),
                observeControllerUseCase = get(),
                readControllerUseCase = get()
        )
    }
}
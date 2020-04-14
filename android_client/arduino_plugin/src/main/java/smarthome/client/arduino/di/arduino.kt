package smarthome.client.arduino.di

import smarthome.client.arduino.controller.di.controllerModule
import smarthome.client.arduino.scripts.di.scriptsModule

val arduino = listOf(
        scriptsModule,
        controllerModule
)
package smarthome.raspberry.di

import smarthome.raspberry.arduinodevices.di.arduinoModule
import smarthome.raspberry.authentication.di.authenticationModule
import smarthome.raspberry.channel.di.channelModule
import smarthome.raspberry.controllers.di.controllersModule
import smarthome.raspberry.devices.di.devicesModule
import smarthome.raspberry.home.di.homeModule
import smarthome.raspberry.notification.di.notificationModule
import smarthome.raspberry.scripts.di.scriptsModule

val appModules = authenticationModule +
                 channelModule +
                 controllersModule +
                 devicesModule +
                 homeModule +
                 notificationModule +
                 scriptsModule +
                 arduinoModule


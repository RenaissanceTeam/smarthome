package smarthome.raspberry.di

import smarthome.raspberry.authentication.di.authenticationModule
import smarthome.raspberry.channel.di.channelModule
import smarthome.raspberry.controllers.di.controllersModule
import smarthome.raspberry.devices.di.devicesModule
import smarthome.raspberry.external.di.externalModule
import smarthome.raspberry.home.di.homeModule
import smarthome.raspberry.input.di.inputModule
import smarthome.raspberry.notification.di.notificationModule
import smarthome.raspberry.scripts.di.scriptsModule
import smarthome.raspberry.util.di.utilModule

val appModules = authenticationModule +
                 channelModule +
                 controllersModule +
                 devicesModule +
                 homeModule +
                 inputModule +
                 notificationModule +
                 scriptsModule +
                 utilModule +
                 externalModule

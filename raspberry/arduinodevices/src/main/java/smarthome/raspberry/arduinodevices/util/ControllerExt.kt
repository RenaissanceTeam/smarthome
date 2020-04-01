package smarthome.raspberry.arduinodevices.util

import smarthome.raspberry.entity.controller.Controller

val Controller.index get() = device.controllers.indexOf(this)
package smarthome.client.presentation

import smarthome.client.entity.Controller


class NoDeviceWithControllerException(controller: Controller) :
    Throwable("No device with controller=$controller")

class NoControllerException(guid: Long) : Throwable("No controller with guid=$guid")

package smarthome.client.presentation

import smarthome.client.domain.api.entity.Controller


class NoDeviceWithControllerException(controller: Controller) : Throwable("No device with controller=$controller")
class NoControllerException(guid: Long) : Throwable("No controller with guid=$guid")

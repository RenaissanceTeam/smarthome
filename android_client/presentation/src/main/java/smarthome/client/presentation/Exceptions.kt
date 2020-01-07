package smarthome.client.presentation

import smarthome.client.entity.HomeException


class NoDeviceWithControllerException(controller: Controller) : HomeException("No device with controller=$controller")
class NoControllerException(guid: Long) : HomeException("No controller with guid=$guid")

package smarthome.client.presentation

import smarthome.client.domain.HomeException
import smarthome.library.common.BaseController


class NoDeviceWithControllerException(controller: BaseController) : HomeException("No device with controller=$controller")
class NoControllerException(guid: Long) : HomeException("No controller with guid=$guid")

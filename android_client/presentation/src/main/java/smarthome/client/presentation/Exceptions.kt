package smarthome.client.presentation

import smarthome.client.domain_api.HomeException
import smarthome.library.common.BaseController


class NoDeviceWithControllerException(controller: BaseController) : smarthome.client.domain_api.HomeException("No device with controller=$controller")
class NoControllerException(guid: Long) : smarthome.client.domain_api.HomeException("No controller with guid=$guid")

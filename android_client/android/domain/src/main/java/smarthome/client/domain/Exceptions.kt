package smarthome.client.domain

import smarthome.library.common.BaseController

open class HomeException(msg: String) : RuntimeException(msg)
class NoDeviceException(guid: Long) : HomeException("No device with guid=$guid")
class NoControllerException(guid: Long) : HomeException("No controller with guid=$guid")
class NoDeviceWithControllerException(controller: BaseController) : HomeException("No device with controller=$controller")
class NoScriptException(guid: Long) : HomeException("No script with guid=$guid")

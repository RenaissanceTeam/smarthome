package smarthome.raspberry

import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice
import kotlin.RuntimeException

class UnableToCreateHomeStorage(homeId: String) : RuntimeException("can't create home devicesStorage for id=$homeId")
class UnableToCreateMessageQueue(homeId: String) : RuntimeException("can't create message queue for home: $homeId")
class UnsupportedRead(controller: BaseController) : RuntimeException("Can't read controller $controller")
class UnsupportedWrite(controller: BaseController, value: String) : RuntimeException("Can't write val=$value to controller $controller ")
class OddDeviceInCloud(device: IotDevice)
    : RuntimeException("Cloud has info about device, but how can raspberry create proper object?! $device")
class OddControllerInCloud(controller: BaseController)
    : RuntimeException("Cloud has info about controller, but how can raspberry create proper object?! $controller")
class AlertException(msg: String): RuntimeException(msg)
class FirestoreUnreachable(): RuntimeException()

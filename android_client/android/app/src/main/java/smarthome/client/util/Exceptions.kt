package smarthome.client.util

import com.google.firebase.auth.FirebaseAuth
import smarthome.library.common.BaseController
import java.lang.RuntimeException

open class HomeModelException(msg: String) : RuntimeException(msg)
class NoDeviceException(guid: Long) : HomeModelException("No device with guid=$guid")
class NoDeviceWithControllerException(controller: BaseController) : HomeModelException("No device with controller=$controller")
class NoControllerException(guid: Long) : HomeModelException("No controller with guid=$guid")
class AuthenticationFailed : HomeModelException("Can't use UID because user is not authorized, current user = ${FirebaseAuth.getInstance().currentUser}")
class NoHomeid : HomeModelException("Can't use firestore, because no homeId for user is found")
class RemoteFailure(e: Exception) : HomeModelException("remote returned: $e")
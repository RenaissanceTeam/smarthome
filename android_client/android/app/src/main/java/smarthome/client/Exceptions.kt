package smarthome.client

import com.google.firebase.auth.FirebaseAuth
import java.lang.RuntimeException

open class HomeModelException(msg: String) : RuntimeException(msg)
class NoDeviceException(guid: Long): HomeModelException("No device with guid=$guid")
class AuthenticationFailed : HomeModelException("Can't use UID because user is not authorized, current user = ${FirebaseAuth.getInstance().currentUser}")
class NoHomeid: HomeModelException("Can't use firestore, because no homeId for user is found")
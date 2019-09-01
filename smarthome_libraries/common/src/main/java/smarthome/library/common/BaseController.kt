package smarthome.library.common

/**
 * Basic controller interface
 * <br></br>All controllers must implement this interface
 */
open class BaseController(var name: String,
                          var state: ControllerState? = null,
                          var serveState: ControllerServeState = ControllerServeState.IDLE,
                          val guid: Long = GUID.getGuidForName(name)) {


    override fun hashCode(): Int {
        return guid.toInt()
    }

    override fun equals(other: Any?): Boolean {
        return (other as? BaseController)?.guid == guid
    }
}

open class ControllerState

enum class ControllerServeState {
    PENDING_READ, PENDING_WRITE, IDLE
}
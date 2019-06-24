package smarthome.library.common

/**
 * Basic controller interface
 * <br></br>All controllers must implement this interface
 */
open class BaseController(var name: String,
                          var state: ControllerState,
                          var serveState: ServeState = ServeState.IDLE,
                          val guid: Long = GUID.getGuidForName(name)) {


    override fun hashCode(): Int {
        return guid.toInt()
    }

    override fun equals(obj: Any?): Boolean {
        return (obj as? BaseController)?.guid == guid
    }
}

open class ControllerState {

}

enum class ServeState {
    PENDING, UPDATING, IDLE
}
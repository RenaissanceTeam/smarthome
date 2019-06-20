package smarthome.library.common

/**
 * Basic controller interface
 * <br></br>All controllers must implement this interface
 */
class BaseController(val name: String,
                     val state: ControllerState,
                     val serveState: ServeState,
                     val guid: Long) {


    override fun hashCode(): Int {
        return guid.toInt()
    }

    override fun equals(obj: Any?): Boolean {
        return (obj as? BaseController)?.guid == guid
    }
}

class ControllerState {

}

enum class ServeState {
    PENDING, UPDATING, IDLE
}
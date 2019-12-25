package smarthome.library.common

/**
 * Basic controller interface
 * <br></br>All controllers must implement this interface
 */
open class BaseController(var name: String,
                          var state: ControllerState? = null,
                          var serveState: ControllerServeState = ControllerServeState.IDLE,
                          @Deprecated("use id instead") val guid: Long = 1L,
                          val id: Id = Id(name)) {


    override fun hashCode() = id.hashCode()

    override fun equals(other: Any?) = (other as? BaseController)?.id == id
}
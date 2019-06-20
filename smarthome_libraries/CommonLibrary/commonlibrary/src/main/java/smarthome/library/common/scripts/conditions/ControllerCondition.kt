//package smarthome.library.common.scripts.conditions
//
//import smarthome.library.common.BaseController
//import smarthome.library.common.constants.*
//
//open class ControllerCondition : Condition() {
//    var chosenController: BaseController? = null
//    var compare: String? = null
//    var value: String? = null
//
//    override fun evaluate(): Boolean {
//        val controller = chosenController ?: return false
//        val value = value ?: return false
//
//        return when (compare) {
//            COMPARE_LESS_THAN -> controller.state.toDouble() < value.toDouble()
//            COMPARE_MORE_THAN -> controller.state.toDouble() > value.toDouble()
//            COMPARE_EQUAL_TO -> controller.state.toDouble() == value.toDouble()
//            else -> throw RuntimeException("Unsupported compare: $compare")
//        }
//    }
//
//    override fun toString() = "Controller ${chosenController?.name} $compare $value"
//}
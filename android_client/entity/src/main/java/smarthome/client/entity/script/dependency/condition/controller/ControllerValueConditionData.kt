package smarthome.client.entity.script.dependency.condition.controller

open class ControllerValueConditionData(
    override val controllerId: Long,
    val value: String? = null,
    val sign: String? = null
) : ControllerConditionData {
    
    fun withSign(sign: String) {
        TODO()
    }
    
    fun withValue(value: String) {
        TODO()
    }
    
}

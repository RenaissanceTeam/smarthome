package smarthome.client.entity.script.dependency.condition.controller

interface ControllerValueConditionData : ControllerConditionData {
    val value: String?
    val sign: String?
}

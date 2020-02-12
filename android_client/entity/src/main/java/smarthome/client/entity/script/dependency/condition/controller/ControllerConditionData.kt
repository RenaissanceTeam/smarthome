package smarthome.client.entity.script.dependency.condition.controller

import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.entity.script.dependency.condition.ConditionData

interface ControllerConditionData: ConditionData {
    val controllerId: Long
}
package smarthome.client.entity.script.dependency.condition.controller

import smarthome.client.entity.script.dependency.condition.Condition

interface ControllerCondition: Condition {
    val controllerId: Long
}
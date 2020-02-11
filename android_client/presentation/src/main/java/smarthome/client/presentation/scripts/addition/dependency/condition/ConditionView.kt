package smarthome.client.presentation.scripts.addition.dependency.condition

import smarthome.client.entity.script.dependency.condition.Condition

interface ConditionView {
    fun getCondition(): Condition
}
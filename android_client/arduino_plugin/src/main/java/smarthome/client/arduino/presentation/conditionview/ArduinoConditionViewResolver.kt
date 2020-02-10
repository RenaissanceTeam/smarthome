package smarthome.client.arduino.presentation.conditionview

import android.content.Context
import smarthome.client.arduino.entity.condition.HumidityCondition
import smarthome.client.arduino.entity.condition.TemperatureCondition
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.presentation.scripts.addition.dependency.condition.ConditionView
import smarthome.client.presentation.scripts.resolver.ConditionViewResolver

class ArduinoConditionViewResolver : ConditionViewResolver {
    override fun canResolve(condition: Condition): Boolean {
        return when (condition) {
            is HumidityCondition,
            is TemperatureCondition -> true
            else -> false
        }
    }
    
    override fun resolve(context: Context, condition: Condition): ConditionView? {
        return when (condition) {
            is HumidityCondition -> HumidityConditionView(context)
            is TemperatureCondition -> TemperatureConditionView(context)
            else -> null
        }
    }
}



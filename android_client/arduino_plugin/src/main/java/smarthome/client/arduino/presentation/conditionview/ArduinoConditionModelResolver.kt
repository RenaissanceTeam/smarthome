package smarthome.client.arduino.presentation.conditionview

import android.content.Context
import com.airbnb.epoxy.EpoxyModel
import smarthome.client.arduino.entity.condition.HumidityCondition
import smarthome.client.arduino.entity.condition.TemperatureCondition
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.presentation.scripts.resolver.ConditionModelResolver

class ArduinoConditionModelResolver : ConditionModelResolver {
    override fun canResolve(condition: Condition): Boolean {
        return when (condition) {
            is HumidityCondition,
            is TemperatureCondition -> true
            else -> false
        }
    }
    
    override fun resolve(condition: Condition): EpoxyModel<*> {
        return when (condition) {
            is HumidityCondition -> HumidityConditionViewModel_().id("humidity")
            is TemperatureCondition -> TemperatureConditionViewModel_().id("temperature")
            else -> throw IllegalArgumentException("can't resolve $condition")
        }
    }
}



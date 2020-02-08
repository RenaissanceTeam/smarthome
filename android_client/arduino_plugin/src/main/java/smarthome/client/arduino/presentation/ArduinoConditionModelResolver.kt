package smarthome.client.arduino.presentation

import com.airbnb.epoxy.EpoxyModel
import smarthome.client.arduino.entity.HumidityCondition
import smarthome.client.arduino.entity.TemperatureCondition
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.presentation.scripts.addition.dependency.ConditionView
import smarthome.client.presentation.scripts.resolver.ConditionModelResolver

class ArduinoConditionModelResolver : ConditionModelResolver {
    override fun canResolve(condition: Condition): Boolean {
        return when (condition) {
            is HumidityCondition,
            is TemperatureCondition -> true
            else -> false
        }
    }
    
    override fun resolve(condition: Condition): EpoxyModel<ConditionView>? {
        TODO()
    }
}
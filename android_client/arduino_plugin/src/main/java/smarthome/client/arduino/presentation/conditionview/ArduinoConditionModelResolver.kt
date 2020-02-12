package smarthome.client.arduino.presentation.conditionview

import com.airbnb.epoxy.EpoxyModel
import smarthome.client.arduino.entity.condition.HumidityConditionData
import smarthome.client.arduino.entity.condition.TemperatureConditionData
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.presentation.scripts.resolver.ConditionModelResolver

class ArduinoConditionModelResolver : ConditionModelResolver {
    override fun canResolve(condition: Condition): Boolean {
        return when (condition.data) {
            is HumidityConditionData,
            is TemperatureConditionData -> true
            else -> false
        }
    }
    
    override fun resolve(condition: Condition): EpoxyModel<*> {
        return when (condition.data) {
            is HumidityConditionData -> HumidityConditionViewModel_().id("humidity")
            is TemperatureConditionData -> TemperatureConditionViewModel_().id("temperature")
            else -> throw IllegalArgumentException("can't resolve $condition")
        }
    }
}



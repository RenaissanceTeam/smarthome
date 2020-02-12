package smarthome.client.arduino.presentation.condition

import com.airbnb.epoxy.EpoxyModel
import smarthome.client.arduino.entity.condition.HumidityConditionData
import smarthome.client.arduino.entity.condition.TemperatureConditionData
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.presentation.scripts.addition.dependency.condition.ControllerConditionValueViewModel_
import smarthome.client.presentation.scripts.resolver.ConditionModelResolver

class ArduinoConditionModelResolver(
    private val humidityConditionModelFactory: HumidityConditionModelFactory,
    private val temperatureConditionModelFactory: TemperatureConditionModelFactory
) : ConditionModelResolver {
    override fun canResolve(condition: Condition): Boolean {
        return when (condition.data) {
            is HumidityConditionData,
            is TemperatureConditionData -> true
            else -> false
        }
    }
    
    override fun resolve(condition: Condition): EpoxyModel<*> {
        return when (condition.data) {
            is HumidityConditionData -> humidityConditionModelFactory.create(condition)
            is TemperatureConditionData -> temperatureConditionModelFactory.create(condition)
            else -> throw IllegalArgumentException("can't resolve $condition")
        }
    }
}



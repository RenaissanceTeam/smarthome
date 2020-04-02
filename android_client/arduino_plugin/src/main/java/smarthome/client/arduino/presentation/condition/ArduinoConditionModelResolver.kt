package smarthome.client.arduino.presentation.condition

import com.airbnb.epoxy.EpoxyModel
import smarthome.client.arduino.entity.condition.HumidityCondition
import smarthome.client.arduino.entity.condition.TemperatureCondition
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.presentation.scripts.resolver.ConditionModelResolver

class ArduinoConditionModelResolver(
    private val humidityConditionModelFactory: HumidityConditionModelFactory,
    private val temperatureConditionModelFactory: TemperatureConditionModelFactory
) : ConditionModelResolver {
    override fun canResolve(item: Condition): Boolean {
        return when (item) {
            is HumidityCondition,
            is TemperatureCondition -> true
            else -> false
        }
    }
    
    override fun resolve(item: Condition): EpoxyModel<*> {
        return when (item) {
            is HumidityCondition -> humidityConditionModelFactory.create(item)
            is TemperatureCondition -> temperatureConditionModelFactory.create(item)
            else -> throw IllegalArgumentException("can't resolve $item")
        }
    }
}



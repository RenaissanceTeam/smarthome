package smarthome.client.arduino.presentation.condition

import com.airbnb.epoxy.EpoxyModel
import smarthome.client.arduino.entity.condition.HumidityConditionData
import smarthome.client.arduino.entity.condition.TemperatureConditionData
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.presentation.scripts.resolver.ConditionModelResolver

class ArduinoConditionModelResolver(
    private val humidityConditionModelFactory: HumidityConditionModelFactory,
    private val temperatureConditionModelFactory: TemperatureConditionModelFactory
) : ConditionModelResolver {
    override fun canResolve(item: Condition): Boolean {
        return when (item.data) {
            is HumidityConditionData,
            is TemperatureConditionData -> true
            else -> false
        }
    }
    
    override fun resolve(item: Condition): EpoxyModel<*> {
        return when (item.data) {
            is HumidityConditionData -> humidityConditionModelFactory.create(item)
            is TemperatureConditionData -> temperatureConditionModelFactory.create(item)
            else -> throw IllegalArgumentException("can't resolve $item")
        }
    }
}



package smarthome.client.arduino.scripts.presentation

import com.airbnb.epoxy.EpoxyModel
import smarthome.client.arduino.scripts.entity.condition.AnalogCondition
import smarthome.client.arduino.scripts.entity.condition.DigitalCondition
import smarthome.client.arduino.scripts.entity.condition.HumidityCondition
import smarthome.client.arduino.scripts.entity.condition.TemperatureCondition
import smarthome.client.arduino.scripts.presentation.analog.AnalogConditionModelFactory
import smarthome.client.arduino.scripts.presentation.dht.HumidityConditionModelFactory
import smarthome.client.arduino.scripts.presentation.dht.TemperatureConditionModelFactory
import smarthome.client.arduino.scripts.presentation.digital.DigitalConditionModelFactory
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.presentation.scripts.resolver.ConditionModelResolver

class ArduinoConditionModelResolver(
        private val humidityConditionModelFactory: HumidityConditionModelFactory,
        private val temperatureConditionModelFactory: TemperatureConditionModelFactory,
        private val analogConditionModelFactory: AnalogConditionModelFactory,
        private val digitalConditionModelFactory: DigitalConditionModelFactory
) : ConditionModelResolver {
    override fun canResolve(item: Condition): Boolean {
        return when (item) {
            is HumidityCondition,
            is AnalogCondition,
            is DigitalCondition,
            is TemperatureCondition -> true
            else -> false
        }
    }

    override fun resolve(item: Condition): EpoxyModel<*> {
        return when (item) {
            is HumidityCondition -> humidityConditionModelFactory.create(item)
            is TemperatureCondition -> temperatureConditionModelFactory.create(item)
            is AnalogCondition -> analogConditionModelFactory.create(item)
            is DigitalCondition -> digitalConditionModelFactory.create(item)
            else -> throw IllegalArgumentException("can't resolve $item")
        }
    }
}



package smarthome.client.arduino.data

import org.koin.core.KoinComponent
import org.koin.core.get
import smarthome.client.arduino.entity.action.OnOffAction
import smarthome.client.arduino.entity.block.ArduinoControllerBlock
import smarthome.client.arduino.entity.condition.HumidityCondition
import smarthome.client.arduino.entity.condition.TemperatureCondition
import smarthome.client.data.api.typeadapter.ActionDataTypeAdapter
import smarthome.client.data.api.typeadapter.BlockTypeAdapter
import smarthome.client.data.api.typeadapter.ConditionTypeAdapter

class ArduinoTypeAdapters : KoinComponent {
    
    init {
        get<BlockTypeAdapter>().setTypes(listOf(ArduinoControllerBlock::class.java))
        get<ConditionTypeAdapter>().setTypes(listOf(
            HumidityCondition::class.java,
            TemperatureCondition::class.java
        ))
        get<ActionDataTypeAdapter>().setTypes(listOf(
            OnOffAction::class.java
        ))
    }
}
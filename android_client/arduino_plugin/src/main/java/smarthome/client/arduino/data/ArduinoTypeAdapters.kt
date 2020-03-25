package smarthome.client.arduino.data

import org.koin.core.KoinComponent
import org.koin.core.get
import smarthome.client.arduino.entity.action.OnOffActionData
import smarthome.client.arduino.entity.block.ArduinoControllerBlock
import smarthome.client.arduino.entity.condition.HumidityConditionData
import smarthome.client.arduino.entity.condition.TemperatureConditionData
import smarthome.client.data.api.typeadapter.ActionDataTypeAdapter
import smarthome.client.data.api.typeadapter.BlockTypeAdapter
import smarthome.client.data.api.typeadapter.ConditionDataTypeAdapter

class ArduinoTypeAdapters : KoinComponent {
    
    init {
        get<BlockTypeAdapter>().setTypes(listOf(ArduinoControllerBlock::class.java))
        get<ConditionDataTypeAdapter>().setTypes(listOf(
            HumidityConditionData::class.java,
            TemperatureConditionData::class.java
        ))
        get<ActionDataTypeAdapter>().setTypes(listOf(
            OnOffActionData::class.java
        ))
    }
}
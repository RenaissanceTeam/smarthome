package smarthome.client.arduino.scripts.data

import org.koin.core.KoinComponent
import org.koin.core.get
import smarthome.client.arduino.scripts.entity.action.OnOffAction
import smarthome.client.arduino.scripts.entity.block.ArduinoControllerBlock
import smarthome.client.arduino.scripts.entity.condition.HumidityCondition
import smarthome.client.arduino.scripts.entity.condition.TemperatureCondition
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
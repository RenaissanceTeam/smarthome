package smarthome.client.arduino.data

import org.koin.core.KoinComponent
import org.koin.core.get
import smarthome.client.arduino.entity.action.OnOffActionData
import smarthome.client.arduino.entity.block.ArduinoControllerBlock
import smarthome.client.arduino.entity.condition.HumidityConditionData
import smarthome.client.arduino.entity.condition.TemperatureConditionData
import smarthome.client.data.api.typeadapter.DataTypeAdapter
import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.dependency.action.ActionData
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.entity.script.dependency.condition.ConditionData

class ArduinoTypeAdapters : KoinComponent {
    
    init {
        get<DataTypeAdapter<Block>>().setTypes(listOf(ArduinoControllerBlock::class.java))
        get<DataTypeAdapter<ConditionData>>().setTypes(listOf(
            HumidityConditionData::class.java,
            TemperatureConditionData::class.java
        ))
        get<DataTypeAdapter<ActionData>>().setTypes(listOf(
            OnOffActionData::class.java
        ))
    }
}
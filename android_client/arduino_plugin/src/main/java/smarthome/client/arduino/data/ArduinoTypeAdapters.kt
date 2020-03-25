package smarthome.client.arduino.data

import org.koin.core.KoinComponent
import org.koin.core.get
import smarthome.client.arduino.entity.block.ArduinoControllerBlock
import smarthome.client.data.api.typeadapter.DataTypeAdapter
import smarthome.client.entity.script.block.Block

class ArduinoTypeAdapters : KoinComponent {
    
    init {
        get<DataTypeAdapter<Block>>().setTypes(listOf(ArduinoControllerBlock::class.java))
    }
}
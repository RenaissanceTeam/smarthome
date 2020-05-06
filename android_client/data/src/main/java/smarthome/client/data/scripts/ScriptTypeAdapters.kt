package smarthome.client.data.scripts

import org.koin.core.KoinComponent
import org.koin.core.get
import smarthome.client.data.api.typeadapter.ActionDataTypeAdapter
import smarthome.client.data.api.typeadapter.BlockTypeAdapter
import smarthome.client.data.api.typeadapter.ConditionTypeAdapter
import smarthome.client.domain.api.scripts.blocks.notification.SendNotificationAction
import smarthome.client.domain.api.scripts.blocks.time.EachDayCondition
import smarthome.client.domain.api.scripts.blocks.time.TimerCondition
import smarthome.client.domain.api.scripts.blocks.time.WeekdaysCondition
import smarthome.client.entity.script.block.LocationBlock
import smarthome.client.entity.script.block.NotificationBlock
import smarthome.client.entity.script.block.TimeBlock

class ScriptTypeAdapters : KoinComponent {
    init {
        get<BlockTypeAdapter>().setTypes(listOf(
                NotificationBlock::class.java,
                TimeBlock::class.java,
                LocationBlock::class.java
        ))
        get<ConditionTypeAdapter>().setTypes(listOf(
                WeekdaysCondition::class.java,
                TimerCondition::class.java,
                EachDayCondition::class.java
        ))
        get<ActionDataTypeAdapter>().setTypes(listOf(
                SendNotificationAction::class.java
        ))
    }
}
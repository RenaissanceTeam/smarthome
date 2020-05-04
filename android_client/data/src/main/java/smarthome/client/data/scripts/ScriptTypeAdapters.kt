package smarthome.client.data.scripts

import org.koin.core.KoinComponent
import org.koin.core.get
import smarthome.client.data.api.typeadapter.ActionDataTypeAdapter
import smarthome.client.data.api.typeadapter.BlockTypeAdapter
import smarthome.client.data.api.typeadapter.ConditionTypeAdapter
import smarthome.client.domain.api.scripts.blocks.notification.SendNotificationAction
import smarthome.client.entity.script.block.NotificationBlock

class ScriptTypeAdapters : KoinComponent {
    init {
        get<BlockTypeAdapter>().setTypes(listOf(
                NotificationBlock::class.java
        ))
        get<ConditionTypeAdapter>().setTypes(listOf(
        ))
        get<ActionDataTypeAdapter>().setTypes(listOf(
                SendNotificationAction::class.java
        ))
    }
}
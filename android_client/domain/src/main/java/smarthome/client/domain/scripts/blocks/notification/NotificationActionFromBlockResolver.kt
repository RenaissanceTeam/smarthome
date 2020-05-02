package smarthome.client.domain.scripts.blocks.notification

import smarthome.client.domain.api.scripts.blocks.SendNotificationAction
import smarthome.client.domain.api.scripts.resolver.ActionFromBlockResolver
import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.block.NotificationBlock
import smarthome.client.entity.script.dependency.action.Action
import smarthome.client.util.generateId

class NotificationActionFromBlockResolver : ActionFromBlockResolver {
    override fun canResolve(item: Block): Boolean {
        return item is NotificationBlock
    }

    override fun resolve(item: Block): List<Action> {
        return listOf(SendNotificationAction(generateId()))
    }
}
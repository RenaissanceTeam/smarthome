package smarthome.client.presentation.scripts.setup.graph.blockviews.notifications

import smarthome.client.domain.api.scripts.resolver.BlockNameResolver
import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.block.NotificationBlock

class NotificationBlockNameResolver : BlockNameResolver {
    override fun canResolve(item: Block): Boolean {
        return item is NotificationBlock
    }

    override fun resolve(item: Block): String {
        return "Notification"
    }
}
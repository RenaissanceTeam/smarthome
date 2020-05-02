package smarthome.client.presentation.scripts.setup.graph.blockviews.factory

import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.qualifier.named
import smarthome.client.entity.script.block.NotificationBlock
import smarthome.client.presentation.scripts.setup.graph.blockviews.controller.ControllerBlockState
import smarthome.client.presentation.scripts.setup.graph.blockviews.notifications.NotificationBlockState
import smarthome.client.presentation.scripts.setup.graph.blockviews.state.BlockState

class GraphBlockFactoryResolverImpl : GraphBlockFactoryResolver, KoinComponent {
    override fun resolve(blockState: BlockState): GraphBlockFactory {
        return when (blockState) {
            is ControllerBlockState -> get(named(CONTROLLER_FACTORY))
            is NotificationBlockState -> get(named(NOTIFICATION_FACTORY))
            else -> throw IllegalStateException("No view factory for graph block $blockState")
        }
    }
}
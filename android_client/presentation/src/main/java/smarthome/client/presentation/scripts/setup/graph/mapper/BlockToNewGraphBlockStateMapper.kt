package smarthome.client.presentation.scripts.setup.graph.mapper

import smarthome.client.entity.script.block.*
import smarthome.client.presentation.scripts.setup.graph.blockviews.controller.ControllerBlockState
import smarthome.client.presentation.scripts.setup.graph.blockviews.location.LocationBlockState
import smarthome.client.presentation.scripts.setup.graph.blockviews.notifications.NotificationBlockState
import smarthome.client.presentation.scripts.setup.graph.blockviews.state.BlockState
import smarthome.client.presentation.scripts.setup.graph.blockviews.time.TimeBlockState

class BlockToNewGraphBlockStateMapper {
    fun map(block: Block): BlockState = when (block) {
        is ControllerBlock -> ControllerBlockState(block)
        is NotificationBlock -> NotificationBlockState(block)
        is LocationBlock -> LocationBlockState(block)
        is TimeBlock -> TimeBlockState(block)
        else -> throw IllegalArgumentException("can't map $block to graph block")
    }
}
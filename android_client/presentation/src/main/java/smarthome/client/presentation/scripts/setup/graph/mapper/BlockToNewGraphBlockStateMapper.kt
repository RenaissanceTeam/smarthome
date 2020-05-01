package smarthome.client.presentation.scripts.setup.graph.mapper

import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.block.NotificationBlock
import smarthome.client.entity.script.controller.ControllerBlock
import smarthome.client.presentation.scripts.setup.graph.blockviews.controller.ControllerBlockState
import smarthome.client.presentation.scripts.setup.graph.blockviews.notifications.NotificationBlockState
import smarthome.client.presentation.scripts.setup.graph.blockviews.state.BlockState

class BlockToNewGraphBlockStateMapper {
    fun map(block: Block): BlockState = when (block) {
        is ControllerBlock -> ControllerBlockState(block)
        is NotificationBlock -> NotificationBlockState(block)
        else -> throw IllegalArgumentException("can't map $block to graph block")
    }
}
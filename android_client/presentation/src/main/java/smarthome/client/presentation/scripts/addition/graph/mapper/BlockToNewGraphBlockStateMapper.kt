package smarthome.client.presentation.scripts.addition.graph.mapper

import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.controller.ControllerBlock
import smarthome.client.presentation.scripts.addition.graph.blockviews.controller.ControllerBlockState
import smarthome.client.presentation.scripts.addition.graph.blockviews.state.BlockState

class BlockToNewGraphBlockStateMapper {
    fun map(block: Block): BlockState = when (block) {
        is ControllerBlock -> ControllerBlockState(block)
        else -> throw IllegalArgumentException("can't map $block to graph block")
    }
}
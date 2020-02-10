package smarthome.client.presentation.scripts.addition.graph.helper

import smarthome.client.entity.script.Block
import smarthome.client.entity.script.controller.ControllerBlock
import smarthome.client.presentation.scripts.addition.graph.blockviews.controller.ControllerBlockState
import smarthome.client.presentation.scripts.addition.graph.blockviews.state.BlockState

class AddGraphBlockStateHelper {
    fun createBlockState(block: Block): BlockState {
        return when (block) {
            is ControllerBlock -> {
                ControllerBlockState(block)
            }
            else -> throw IllegalArgumentException("can't create block state for $block")
        }
    }
}
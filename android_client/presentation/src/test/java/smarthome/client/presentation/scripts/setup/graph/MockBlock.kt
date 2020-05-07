package smarthome.client.presentation.scripts.setup.graph

import smarthome.client.entity.script.block.Block

import smarthome.client.util.Position

data class MockBlock(override val id: String, override val position: Position) : Block {

    override fun copyWithPosition(newPosition: Position): Block {
        return copy(position = newPosition)
    }
}
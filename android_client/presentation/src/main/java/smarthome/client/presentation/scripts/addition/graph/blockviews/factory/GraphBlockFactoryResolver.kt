package smarthome.client.presentation.scripts.addition.graph.blockviews.factory

import smarthome.client.presentation.scripts.addition.graph.blockviews.state.BlockState

interface GraphBlockFactoryResolver {
    fun resolve(blockState: BlockState): GraphBlockFactory
}
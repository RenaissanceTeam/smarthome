package smarthome.client.presentation.scripts.setup.graph.blockviews.factory

import smarthome.client.presentation.scripts.setup.graph.blockviews.state.BlockState

interface GraphBlockFactoryResolver {
    fun resolve(blockState: BlockState): GraphBlockFactory
}
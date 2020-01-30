package smarthome.client.presentation.scripts.addition.graph.blockviews.factory

import smarthome.client.presentation.scripts.addition.graph.blockviews.state.GraphBlock

interface GraphBlockFactoryResolver {
    fun resolve(block: GraphBlock): GraphBlockFactory
}
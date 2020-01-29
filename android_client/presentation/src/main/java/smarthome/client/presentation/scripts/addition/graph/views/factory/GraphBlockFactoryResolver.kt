package smarthome.client.presentation.scripts.addition.graph.views.factory

import smarthome.client.presentation.scripts.addition.graph.views.state.GraphBlock

interface GraphBlockFactoryResolver {
    fun resolve(block: GraphBlock): GraphBlockFactory
}
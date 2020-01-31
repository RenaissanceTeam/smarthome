package smarthome.client.presentation.scripts.addition.graph.blockviews.factory

import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.qualifier.named
import smarthome.client.presentation.scripts.addition.graph.blockviews.controller.ControllerBlock
import smarthome.client.presentation.scripts.addition.graph.blockviews.state.GraphBlock

class GraphBlockFactoryResolverImpl : GraphBlockFactoryResolver, KoinComponent {
    override fun resolve(block: GraphBlock): GraphBlockFactory {
        return when (block) {
            is ControllerBlock -> get(named(CONTROLLER_FACTORY))
            else -> throw IllegalStateException("No view factory for graph block $block")
        }
    }
}
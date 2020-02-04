package smarthome.client.presentation.scripts.addition.graph.blockviews.factory

import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.qualifier.named
import smarthome.client.presentation.scripts.addition.graph.blockviews.controller.ControllerBlockState
import smarthome.client.presentation.scripts.addition.graph.blockviews.state.BlockState

class GraphBlockFactoryResolverImpl : GraphBlockFactoryResolver, KoinComponent {
    override fun resolve(blockState: BlockState): GraphBlockFactory {
        return when (blockState) {
            is ControllerBlockState -> get(named(CONTROLLER_FACTORY))
            else -> throw IllegalStateException("No view factory for graph block $blockState")
        }
    }
}
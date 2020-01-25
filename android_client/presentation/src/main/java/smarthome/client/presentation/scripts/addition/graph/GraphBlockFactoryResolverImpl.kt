package smarthome.client.presentation.scripts.addition.graph

import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.qualifier.named

class GraphBlockFactoryResolverImpl : GraphBlockFactoryResolver, KoinComponent {
    override fun resolve(block: GraphBlock): GraphBlockFactory? {
        return runCatching { get<GraphBlockFactory>(named(block.type)) }.getOrNull()
    }
}
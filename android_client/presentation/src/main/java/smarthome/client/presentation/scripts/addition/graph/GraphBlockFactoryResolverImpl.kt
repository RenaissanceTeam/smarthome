package smarthome.client.presentation.scripts.addition.graph

import android.view.ViewGroup
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.qualifier.named

class GraphBlockFactoryResolverImpl : GraphBlockFactoryResolver, KoinComponent {
    override fun resolve(block: GraphBlock): GraphBlockFactory {
        return runCatching {
            get<GraphBlockFactory>(named(block.type))
        }.getOrDefault(StubBlockFactory)
    }
}

private object StubBlockFactory : GraphBlockFactory {
    override fun inflate(into: ViewGroup, block: GraphBlock) = StubDraggable
}

private object StubDraggable : GraphDraggable {
    override fun moveTo(position: Position) {}
}
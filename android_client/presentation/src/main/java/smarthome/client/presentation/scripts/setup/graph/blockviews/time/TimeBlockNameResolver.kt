package smarthome.client.presentation.scripts.setup.graph.blockviews.time

import smarthome.client.domain.api.scripts.resolver.BlockNameResolver
import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.block.TimeBlock

class TimeBlockNameResolver : BlockNameResolver {
    override fun canResolve(item: Block): Boolean {
        return item is TimeBlock
    }

    override fun resolve(item: Block): String {
        return "Time"
    }
}
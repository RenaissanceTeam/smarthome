package smarthome.client.presentation.scripts.setup.graph.blockviews.location

import smarthome.client.domain.api.scripts.resolver.BlockNameResolver
import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.block.LocationBlock

class LocationBlockNameResolver : BlockNameResolver {
    override fun canResolve(item: Block): Boolean {
        return item is LocationBlock
    }

    override fun resolve(item: Block): String {
        return "Location"
    }
}

package smarthome.client.presentation.scripts.setup.graph.blockviews.location

import android.view.ViewGroup
import smarthome.client.presentation.scripts.setup.graph.blockviews.GraphBlockView
import smarthome.client.presentation.scripts.setup.graph.blockviews.factory.GraphBlockFactory
import smarthome.client.presentation.scripts.setup.graph.blockviews.state.BlockState

class LocationBlockFactory : GraphBlockFactory {
    override fun inflate(into: ViewGroup, blockState: BlockState): GraphBlockView {
        require(blockState is LocationBlockState)

        return LocationBlockView(into.context).apply { into.addView(this) }
    }
}

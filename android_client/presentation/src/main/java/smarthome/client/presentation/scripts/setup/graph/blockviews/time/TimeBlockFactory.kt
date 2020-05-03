package smarthome.client.presentation.scripts.setup.graph.blockviews.time

import android.view.ViewGroup
import smarthome.client.presentation.scripts.setup.graph.blockviews.GraphBlockView
import smarthome.client.presentation.scripts.setup.graph.blockviews.factory.GraphBlockFactory
import smarthome.client.presentation.scripts.setup.graph.blockviews.state.BlockState

class TimeBlockFactory : GraphBlockFactory {
    override fun inflate(into: ViewGroup, blockState: BlockState): GraphBlockView {
        require(blockState is TimeBlockState)

        return TimeBlockView(into.context).apply { into.addView(this) }
    }
}

package smarthome.client.presentation.scripts.setup.graph.blockviews.notifications

import android.view.ViewGroup
import smarthome.client.presentation.scripts.setup.graph.blockviews.GraphBlockView
import smarthome.client.presentation.scripts.setup.graph.blockviews.factory.GraphBlockFactory
import smarthome.client.presentation.scripts.setup.graph.blockviews.state.BlockState

class NotificationBlockFactory : GraphBlockFactory {
    override fun inflate(into: ViewGroup, blockState: BlockState): GraphBlockView {
        require(blockState is NotificationBlockState)

        return NotificationBlockView(into.context).apply { into.addView(this) }
    }
}

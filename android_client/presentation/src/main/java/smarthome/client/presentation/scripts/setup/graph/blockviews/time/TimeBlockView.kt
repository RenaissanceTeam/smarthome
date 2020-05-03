package smarthome.client.presentation.scripts.setup.graph.blockviews.time

import android.content.Context
import android.util.AttributeSet
import androidx.lifecycle.LifecycleOwner
import smarthome.client.presentation.R
import smarthome.client.presentation.scripts.setup.graph.blockviews.GraphBlockView
import smarthome.client.presentation.scripts.setup.graph.blockviews.state.BlockState

class TimeBlockView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : GraphBlockView(R.layout.scripts_time_block, context, attrs, defStyleAttr) {

    override fun onData(state: BlockState) {}
    override fun onObserveViewModel(lifecycleOwner: LifecycleOwner) {}
}

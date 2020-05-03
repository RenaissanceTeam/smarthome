package smarthome.client.presentation.scripts.setup.graph.blockviews.location

import android.content.Context
import android.util.AttributeSet
import androidx.lifecycle.LifecycleOwner
import smarthome.client.presentation.R
import smarthome.client.presentation.scripts.setup.graph.blockviews.GraphBlockView
import smarthome.client.presentation.scripts.setup.graph.blockviews.state.BlockState

class LocationBlockView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : GraphBlockView(R.layout.scripts_location_block, context, attrs, defStyleAttr) {

    override fun onData(state: BlockState) {}
    override fun onObserveViewModel(lifecycleOwner: LifecycleOwner) {}
}

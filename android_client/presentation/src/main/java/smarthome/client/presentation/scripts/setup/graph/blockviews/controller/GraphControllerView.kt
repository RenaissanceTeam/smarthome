package smarthome.client.presentation.scripts.setup.graph.blockviews.controller

import android.content.Context
import android.util.AttributeSet
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.observe
import kotlinx.android.synthetic.main.scripts_controller_item.view.*
import smarthome.client.entity.Controller
import smarthome.client.presentation.R
import smarthome.client.presentation.scripts.setup.graph.blockviews.GraphBlockView
import smarthome.client.presentation.scripts.setup.graph.blockviews.state.BlockState

class GraphControllerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : GraphBlockView(R.layout.scripts_controller_item, context, attrs, defStyleAttr) {
    private val viewModel = GraphControllerViewModel(baseViewModel)
    
    override fun onData(state: BlockState) {
        if (state !is ControllerBlockState) return
        viewModel.onNewBlockData(state)
    }
    
    override fun onObserveViewModel(lifecycleOwner: LifecycleOwner) {
        viewModel.data.observe(lifecycleOwner, ::bind)
    }
    
    private fun bind(controller: Controller) {
        controller_name.text = controller.name
        controller_state.text = controller.state
    }
}

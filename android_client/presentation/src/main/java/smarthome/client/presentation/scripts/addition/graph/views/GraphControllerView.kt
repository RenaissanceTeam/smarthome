package smarthome.client.presentation.scripts.addition.graph.views

import android.content.ClipData
import android.content.Context
import android.util.AttributeSet
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.widget.FrameLayout
import smarthome.client.presentation.R
import smarthome.client.presentation.scripts.addition.graph.GraphBlockView
import smarthome.client.presentation.scripts.addition.graph.Position
import smarthome.client.presentation.scripts.addition.graph.views.state.ControllerBlock
import smarthome.client.presentation.scripts.addition.graph.views.state.GraphBlock
import smarthome.client.presentation.util.CustomDragShadowBuilder
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.util.inflate

class GraphControllerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), GraphBlockView {
    private val viewModel = GraphControllerViewModel()
    
    init {
        inflate(R.layout.scripts_controller_item)
        
        setOnTouchListener { _, event ->
            if (event.action != MotionEvent.ACTION_DOWN) return@setOnTouchListener false
    
            val info = onDragStarted?.let {it.invoke(Position(event.x, event.y))} ?: return@setOnTouchListener false
    
            val data = ClipData.newPlainText("sad", "asdf")
            val shadowBuilder = CustomDragShadowBuilder(this@GraphControllerView, event)
            performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
            startDrag(data, shadowBuilder, info, 0)
        }
    }
    
    override fun setData(block: GraphBlock) {
        if (block !is ControllerBlock) return
        
        viewModel.onNewBlockData(block)
    }
    
    private fun moveTo(position: Position) {
        x = position.x
        y = position.y
        
        invalidate()
    }
}

class GraphControllerViewModel: KoinViewModel() {
    
    fun onNewBlockData(block: ControllerBlock) {
        // todo on first init observe controller
        // add live data for position
        // add live data for state
    }
    
    fun onDragStarted() {
        // todo publish event
        // from == GRAPH
    }
}
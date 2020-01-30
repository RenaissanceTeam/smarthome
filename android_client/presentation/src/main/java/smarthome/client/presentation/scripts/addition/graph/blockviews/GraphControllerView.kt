package smarthome.client.presentation.scripts.addition.graph.blockviews

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.observe
import kotlinx.android.synthetic.main.scripts_controller_item.view.*
import smarthome.client.entity.Controller
import smarthome.client.presentation.R
import smarthome.client.presentation.scripts.addition.graph.blockviews.state.ControllerBlock
import smarthome.client.presentation.scripts.addition.graph.blockviews.state.GraphBlock
import smarthome.client.presentation.util.CustomDragShadowBuilder
import smarthome.client.presentation.util.Position
import smarthome.client.presentation.util.inflate
import smarthome.client.presentation.util.lifecycleOwner
import smarthome.client.presentation.visible
import smarthome.client.util.log

class GraphControllerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr),
    GraphBlockView {
    private val viewModel = GraphControllerViewModel()
    
    init {
        inflate(R.layout.scripts_controller_item)
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
    
        setOnLongClickListener {
            log("on long")
            true
        }
    
        drag_handle.setOnTouchListener { _, event ->
            if (event.action != MotionEvent.ACTION_DOWN) return@setOnTouchListener false
            
            val touchX = (drag_handle.x + event.x).toInt()
            val touchY = (drag_handle.y + event.y).toInt()
            
            val info = viewModel
                .onDragStarted(Position(touchX.toFloat(), touchY.toFloat())) ?: return@setOnTouchListener false
        
          
            val shadowBuilder = CustomDragShadowBuilder(this@GraphControllerView, touchX, touchY)
        
            startDrag(null, shadowBuilder, info, 0)
        }
    }
    
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        
        lifecycleOwner?.let(::observeViewModel)
    }
    
    private fun observeViewModel(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(viewModel)
        
        viewModel.visible.distinctUntilChanged().observe(lifecycleOwner) { visible = it }
        viewModel.position.distinctUntilChanged().observe(lifecycleOwner, ::moveTo)
        viewModel.data.observe(lifecycleOwner, ::bind)
        viewModel.loading.distinctUntilChanged().observe(lifecycleOwner, ::changeProgress)
        viewModel.dragVisible.observe(lifecycleOwner) { drag_handle.visible = it }
    }
    
    override fun setData(block: GraphBlock) {
        if (block !is ControllerBlock) return
        
        viewModel.onNewBlockData(block)
    }
    
    private fun bind(controller: Controller) {
        controller_name.text = controller.name
        controller_state.text = controller.state
    }
    
    private fun changeProgress(isLoading: Boolean) {
        controller_progress.visible = isLoading
    }
    
    private fun moveTo(position: Position) {
        x = position.x
        y = position.y
        
        invalidate()
    }
}


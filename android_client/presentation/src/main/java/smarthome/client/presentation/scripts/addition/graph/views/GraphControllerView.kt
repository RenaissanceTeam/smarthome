package smarthome.client.presentation.scripts.addition.graph.views

import android.content.ClipData
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
import smarthome.client.presentation.scripts.addition.graph.GraphBlockView
import smarthome.client.presentation.scripts.addition.graph.Position
import smarthome.client.presentation.scripts.addition.graph.views.state.ControllerBlock
import smarthome.client.presentation.scripts.addition.graph.views.state.GraphBlock
import smarthome.client.presentation.util.CustomDragShadowBuilder
import smarthome.client.presentation.util.inflate
import smarthome.client.presentation.util.lifecycleOwner
import smarthome.client.presentation.visible

class GraphControllerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), GraphBlockView {
    private val viewModel = GraphControllerViewModel()
    
    init {
        inflate(R.layout.scripts_controller_item)
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        
        
        setOnTouchListener { _, event ->
            if (event.action != MotionEvent.ACTION_DOWN) return@setOnTouchListener false
            val info = viewModel.onDragStarted(Position(event.x, event.y)) ?: return@setOnTouchListener false

            val data = ClipData.newPlainText("sad", "asdf")
            val shadowBuilder = CustomDragShadowBuilder(this@GraphControllerView, event)
    
            startDrag(data, shadowBuilder, info, 0)
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

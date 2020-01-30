package smarthome.client.presentation.scripts.addition.graph.views

import android.content.ClipData
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.observe
import org.koin.core.inject
import smarthome.client.presentation.R
import smarthome.client.presentation.scripts.addition.graph.GraphBlockView
import smarthome.client.presentation.scripts.addition.graph.Position
import smarthome.client.presentation.scripts.addition.graph.events.GraphEventBus
import smarthome.client.presentation.scripts.addition.graph.events.drag.CommonDragInfo
import smarthome.client.presentation.scripts.addition.graph.events.drag.ControllerDragEvent
import smarthome.client.presentation.scripts.addition.graph.events.drag.DRAG_START
import smarthome.client.presentation.scripts.addition.graph.events.drag.GRAPH
import smarthome.client.presentation.scripts.addition.graph.views.state.ControllerBlock
import smarthome.client.presentation.scripts.addition.graph.views.state.GraphBlock
import smarthome.client.presentation.util.CustomDragShadowBuilder
import smarthome.client.presentation.util.KoinViewModel
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
        
        viewModel.visible.distinctUntilChanged().observe(lifecycleOwner) { visible = it }
        viewModel.position.distinctUntilChanged().observe(lifecycleOwner, ::moveTo)
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
    private val eventBus: GraphEventBus by inject()
    private var id: Long? = null
    val visible = MutableLiveData<Boolean>()
    val position = MutableLiveData<Position>()
    
    fun onNewBlockData(block: ControllerBlock) {
        // todo on first init observe controller
        id = block.id.id
        visible.value = block.visible
        position.value = block.position
        
        // add live data for state
    }
    
    fun onDragStarted(dragTouch: Position): ControllerDragEvent? {
        return id?.let { id ->
            ControllerDragEvent(id = id, dragInfo = CommonDragInfo(
                status = DRAG_START,
                dragTouch = dragTouch,
                from = GRAPH
            )).also(eventBus::addEvent)
        }
    }
}
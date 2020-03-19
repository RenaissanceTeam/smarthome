package smarthome.client.presentation.scripts.addition.graph.blockviews

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.observe
import kotlinx.android.synthetic.main.scripts_block_item.view.*
import smarthome.client.entity.script.block.BlockId
import smarthome.client.presentation.R
import smarthome.client.presentation.scripts.addition.graph.blockviews.state.BlockState
import smarthome.client.presentation.scripts.addition.graph.blockviews.state.BorderStatus
import smarthome.client.presentation.util.*
import smarthome.client.presentation.util.drag.Draggable
import smarthome.client.presentation.util.drag.DraggableTrigger
import smarthome.client.presentation.util.drag.DraggableView
import smarthome.client.util.Position
import smarthome.client.util.emptyPosition
import smarthome.client.util.visible

abstract class GraphBlockView @JvmOverloads constructor(
    contentLayout: Int,
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    protected val baseViewModel = GraphBlockViewModel()
    val centerPosition: Position get() = (baseViewModel.position.value ?: emptyPosition) + center
    var draggable: Draggable? = null
    
    init {
        inflate(R.layout.scripts_block_item)
        block_content.inflate(contentLayout)
        
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
    
        draggable = DraggableView(this, drag_handle, DraggableTrigger.TOUCH)
    }
    
    fun contains(position: Position) = isPositionInside(position)
    
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        
        ViewTreeLifecycleOwner.get(this)?.let(::observeViewModel)
    }
    
    private fun observeViewModel(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(baseViewModel)
        
        baseViewModel.visible.distinctUntilChanged().observe(lifecycleOwner) { visible = it }
        baseViewModel.position.distinctUntilChanged().observe(lifecycleOwner, ::moveTo)
        baseViewModel.loading.distinctUntilChanged().observe(lifecycleOwner, ::changeProgress)
        baseViewModel.dragVisible.observe(lifecycleOwner) { drag_handle.visible = it }
        baseViewModel.blockId.distinctUntilChanged().observe(lifecycleOwner, ::onBlockChanged)
        baseViewModel.border.distinctUntilChanged().observe(lifecycleOwner, ::bindBorderStatus)
        
        onObserveViewModel(lifecycleOwner)
    }
    
    abstract fun onObserveViewModel(lifecycleOwner: LifecycleOwner)
    
    private fun bindBorderStatus(borderStatus: BorderStatus) {
        if (!borderStatus.isVisible) {
            showBorder(Color.TRANSPARENT)
            return
        }
        
        when (borderStatus.isFailure) {
            true -> showBorder(Color.RED)
            false -> showBorder(Color.GREEN)
        }
    }
    
    private fun showBorder(color: Int) {
        val border = GradientDrawable()
        border.setColor(Color.TRANSPARENT)
        border.setStroke(5, color)
        controller_item_content.background = border
    }
    
    private fun onBlockChanged(newId: BlockId) {
        setupLongPressToStartDependency(
            id = newId,
            view = this,
            eventPublisher = baseViewModel
        )
    }
    
    fun setData(blockState: BlockState) {
        baseViewModel.onNewBlockData(blockState)
        onData(blockState)
    }
    
    abstract fun onData(state: BlockState)
    
    private fun changeProgress(isLoading: Boolean) {
        progress.visible = isLoading
    }
    
    private fun moveTo(position: Position) {
        x = position.x.toFloat()
        y = position.y.toFloat()
        
        debug_item.text = "$x, $y, width=$width, height=$height"
    }
    
}
package smarthome.client.presentation.scripts.addition.graph.blockviews

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.observe
import kotlinx.android.synthetic.main.scripts_block_item.view.*
import smarthome.client.entity.script.block.BlockId
import smarthome.client.presentation.R
import smarthome.client.presentation.scripts.addition.graph.blockviews.state.BlockState
import smarthome.client.presentation.scripts.addition.graph.blockviews.state.BorderStatus
import smarthome.client.presentation.util.*
import smarthome.client.util.Position
import smarthome.client.util.emptyPosition
import smarthome.client.util.visible

abstract class GraphBlockView @JvmOverloads constructor(
    private val contentLayout: Int,
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    protected val baseViewModel = GraphBlockViewModel()
    val centerPosition: Position get() = (baseViewModel.position.value ?: emptyPosition) + center
    
    init {
        inflate(R.layout.scripts_block_item)
        block_content.inflate(contentLayout)
        
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        
        handleStartDragging()
    }
    
    private fun handleStartDragging() {
        drag_handle.setOnTouchListener { _, event ->
            if (event.action != MotionEvent.ACTION_DOWN) return@setOnTouchListener false
            
            val touchX = (drag_handle.x + event.x).toInt()
            val touchY = (drag_handle.y + event.y).toInt()
            
            val info = baseViewModel.onDragStarted(Position(touchX, touchY))
                ?: return@setOnTouchListener false
            
            
            val shadowBuilder = CustomDragShadowBuilder(this@GraphBlockView, touchX, touchY)
            
            startDrag(null, shadowBuilder, info, 0)
        }
    }
    
    fun contains(position: Position) = isPositionInside(position)
    
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        
        lifecycleOwner?.let(::observeViewModel)
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
            hideBorder()
            return
        }
        
        when (borderStatus.isFailure) {
            true -> showRedBorder()
            false -> showGreenBorder()
        }
    }
    
    private fun hideBorder() {
        showBorder(Color.TRANSPARENT)
    }
    
    private fun showGreenBorder() {
        showBorder(Color.GREEN)
    }
    
    private fun showRedBorder() {
        showBorder(Color.RED)
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
        ) { }
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
        invalidate()
    }
}
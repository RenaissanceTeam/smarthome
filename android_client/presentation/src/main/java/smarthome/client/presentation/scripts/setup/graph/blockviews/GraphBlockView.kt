package smarthome.client.presentation.scripts.setup.graph.blockviews

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.observe
import com.jakewharton.rxbinding3.view.touches
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.scripts_block_item.view.*
import smarthome.client.presentation.R
import smarthome.client.presentation.scripts.setup.graph.blockviews.state.BlockState
import smarthome.client.presentation.scripts.setup.graph.blockviews.state.BorderStatus
import smarthome.client.presentation.util.drag.Draggable
import smarthome.client.presentation.util.drag.DraggableTrigger
import smarthome.client.presentation.util.drag.DraggableView
import smarthome.client.presentation.util.inflate
import smarthome.client.presentation.util.isPositionInside
import smarthome.client.util.Position
import smarthome.client.util.fadeVisibility
import smarthome.client.util.log
import smarthome.client.util.visible

abstract class GraphBlockView @JvmOverloads constructor(
        contentLayout: Int,
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    protected val baseViewModel = GraphBlockViewModel()
    var draggable: Draggable? = null
    private val toDispose = CompositeDisposable()
    private val listenerDisposable = CompositeDisposable()
    private var blockTouches: Observable<MotionEvent>

    init {
        inflate(R.layout.scripts_block_item)
        block_content.inflate(contentLayout)

        layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)

        draggable = DraggableView(this, drag_handle, DraggableTrigger.TOUCH)

        val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent?): Boolean {
                unit_menu.fadeVisibility(true, duration = 200)
                return true
            }
        })

        blockTouches = touches { true }.doOnNext {
            gestureDetector.onTouchEvent(it)
        }

        unit_menu.setOnClickListener { unit_menu.fadeVisibility(false, duration = 200) }
        delete.setOnClickListener { baseViewModel.onDeleteBlock() }
    }


    fun contains(position: Position) = isPositionInside(position)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        ViewTreeLifecycleOwner.get(this)?.let(::observeViewModel)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        toDispose.dispose()
    }

    private fun observeViewModel(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(baseViewModel)

        baseViewModel.visible.distinctUntilChanged().observe(lifecycleOwner) { visible = it }
        baseViewModel.position.distinctUntilChanged().observe(lifecycleOwner, ::moveTo)
        baseViewModel.loading.distinctUntilChanged().observe(lifecycleOwner, ::changeProgress)
        baseViewModel.dragVisible.observe(lifecycleOwner) { drag_handle.visible = it }
        baseViewModel.blockUuid.distinctUntilChanged().observe(lifecycleOwner, ::onBlockChanged)
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

    private fun onBlockChanged(newUuid: String) {
        listenerDisposable.clear()

        setupLongPressToStartDependency(
                uuid = newUuid,
                view = this,
                eventPublisher = baseViewModel,
                touches = blockTouches,
                listenerDisposable = listenerDisposable
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
    }
}
package smarthome.client.presentation.scripts.addition.graph.controllers.epoxy

import android.content.ClipData
import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.widget.FrameLayout
import com.airbnb.epoxy.*
import kotlinx.android.synthetic.main.scripts_controller_item.view.*
import smarthome.client.presentation.R
import smarthome.client.presentation.scripts.addition.graph.GraphBlock
import smarthome.client.presentation.scripts.addition.graph.GraphDraggable
import smarthome.client.presentation.scripts.addition.graph.Position
import smarthome.client.presentation.util.CustomDragShadowBuilder
import smarthome.client.presentation.util.inflate
import smarthome.client.presentation.visible
import smarthome.client.util.log


@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class ControllerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), GraphDraggable {
    
    init {
        inflate(R.layout.scripts_controller_item)
        val detector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onLongPress(pressEvent: MotionEvent) {
                val data = ClipData.newPlainText("sad", "asdf")
                val shadowBuilder = CustomDragShadowBuilder(this@ControllerView, pressEvent)
                performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                startDrag(data, shadowBuilder, this@ControllerView, 0)
                dragTouchPosition =
                    Position(
                        pressEvent.x, pressEvent.y)
                
                visible = false
            }
        })
        setOnTouchListener { _, event ->
            detector.onTouchEvent(event)
            
            when (event.action) {
                MotionEvent.ACTION_UP,
                MotionEvent.ACTION_MOVE,
                MotionEvent.ACTION_DOWN -> true
                else -> false
            }
        }
    }
    
    lateinit var name: CharSequence @TextProp set
    lateinit var state: CharSequence @TextProp set
    var controllerId: Long = 0 @ModelProp set
    var onDraggedToGraph: (() -> Unit)? = null @CallbackProp set
    private var dragTouchPosition = DEFAULT_TOUCH_POINT
    
    override fun onDraggedToGraph() {
        onDraggedToGraph?.invoke()
    }
    
    override fun onDragCancelled() {
        visible = true
    }
    
    override fun getBlock() = GraphBlock(id = controllerId, type = "controller")
    
    override fun moveTo(position: Position) {
        log("move to $position")
        x = position.x
        y = position.y
        invalidate()
    }
    
    @AfterPropsSet
    fun onPropsReady() {
        dragTouchPosition = DEFAULT_TOUCH_POINT
        visible = true
        controller_name.text = name
        controller_state.text = state
    }
    
    companion object {
        private val DEFAULT_TOUCH_POINT =
            Position(0f, 0f)
    }
 }
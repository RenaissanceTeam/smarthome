package smarthome.client.presentation.scripts.addition.controllers.epoxy

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
import smarthome.client.presentation.scripts.addition.graph.events.drag.DragOperationInfo
import smarthome.client.presentation.scripts.addition.graph.Position
import smarthome.client.presentation.util.CustomDragShadowBuilder
import smarthome.client.presentation.util.inflate


@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class ControllerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    
    init {
        inflate(R.layout.scripts_controller_item)
        val detector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onLongPress(pressEvent: MotionEvent) {
                val info = onDragStarted?.invoke(Position(pressEvent.x, pressEvent.y))

                val data = ClipData.newPlainText("sad", "asdf")
                val shadowBuilder = CustomDragShadowBuilder(this@ControllerView, pressEvent)
                performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                startDrag(data, shadowBuilder, info, 0)
                
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
    
    var onDragStarted: ((Position) -> DragOperationInfo)? = null @CallbackProp set

    @AfterPropsSet
    fun onPropsReady() {
        controller_name.text = name
        controller_state.text = state
    }
 }
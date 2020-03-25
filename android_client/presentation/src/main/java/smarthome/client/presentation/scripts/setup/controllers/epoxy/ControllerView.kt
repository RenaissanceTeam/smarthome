package smarthome.client.presentation.scripts.setup.controllers.epoxy

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.widget.FrameLayout
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import kotlinx.android.synthetic.main.scripts_block_item.view.*
import kotlinx.android.synthetic.main.scripts_controller_item.view.*
import smarthome.client.presentation.R
import smarthome.client.presentation.scripts.setup.graph.events.drag.BlockDragInfo
import smarthome.client.presentation.util.CustomDragShadowBuilder
import smarthome.client.presentation.util.extensions.position
import smarthome.client.presentation.util.inflate
import smarthome.client.util.Position


@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class ControllerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    
    init {
        inflate(R.layout.scripts_block_item)
        block_content.inflate(R.layout.scripts_controller_item)
        
        val detector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onLongPress(pressEvent: MotionEvent) {
                val info = onDragStarted?.invoke(pressEvent.position)

                val shadowBuilder = CustomDragShadowBuilder(this@ControllerView,
                    pressEvent.x.toInt(),
                    pressEvent.y.toInt()
                )
                performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                startDrag(null, shadowBuilder, info, 0)
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
    
    var onDragStarted: ((Position) -> BlockDragInfo)? = null @CallbackProp set

    @AfterPropsSet
    fun onPropsReady() {
        controller_name.text = name
        controller_state.text = state
    }
 }
package smarthome.client.presentation.scripts.addition.graph.blockviews.dependency

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import com.airbnb.epoxy.ModelView
import smarthome.client.presentation.R
import smarthome.client.presentation.position
import smarthome.client.presentation.util.inflate
import smarthome.client.util.log

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class DraggingTipOfDependency @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    
    init {
        inflate(R.layout.empty)
        
        setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_MOVE -> {
                    log("moved tip to ${event.position}")
                    
                    true
                }
                else -> false
            }
        }
    }
}
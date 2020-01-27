package smarthome.client.presentation.scripts.addition.graph.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.airbnb.epoxy.ModelView
import smarthome.client.presentation.R
import smarthome.client.presentation.scripts.addition.graph.GraphDraggable
import smarthome.client.presentation.scripts.addition.graph.Position
import smarthome.client.presentation.util.inflate

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class GraphControllerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), GraphDraggable {
    
    init {
        inflate(R.layout.scripts_controller_item)
    }
    
    override fun moveTo(position: Position) {
        x = position.x
        y = position.y
        
        invalidate()
    }
}
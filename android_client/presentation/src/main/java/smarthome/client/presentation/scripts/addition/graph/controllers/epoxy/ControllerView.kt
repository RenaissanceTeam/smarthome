package smarthome.client.presentation.scripts.addition.graph.controllers.epoxy

import android.content.ClipData
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import kotlinx.android.synthetic.main.scripts_controller_item.view.*
import smarthome.client.presentation.R
import smarthome.client.presentation.scripts.addition.graph.GraphDraggable
import smarthome.client.presentation.util.inflate
import smarthome.client.presentation.visible


@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class ControllerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), GraphDraggable {
    
    init {
        inflate(R.layout.scripts_controller_item)
        setOnLongClickListener {
            val data = ClipData.newPlainText("sad", "asdf")
            val shadowBuilder = DragShadowBuilder(this)
            startDrag(data, shadowBuilder, this, 0)
            visible = false
            
            true
        }
    }
    
    lateinit var name: CharSequence @TextProp set
    lateinit var state: CharSequence @TextProp set
    var onDraggedToGraph: (() -> Unit)? = null @CallbackProp set
    
    override fun onDraggedToGraph() {
        onDraggedToGraph?.invoke()
    }
    
    override fun onDragCancelled() {
        visible = true
    }
    
    @AfterPropsSet
    fun onPropsReady() {
        visible = true
        controller_name.text = name
        controller_state.text = state
    }
 }
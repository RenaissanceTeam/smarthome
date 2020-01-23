package smarthome.client.presentation.scripts.addition.graph.controllers.epoxy

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.airbnb.epoxy.*
import kotlinx.android.synthetic.main.scripts_controller_item.view.*
import smarthome.client.presentation.R
import smarthome.client.presentation.util.inflate

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class ControllerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    
    init {
        inflate(R.layout.scripts_controller_item)
    }
    
    lateinit var name: CharSequence @TextProp set
    lateinit var state: CharSequence @TextProp set
    
    @AfterPropsSet
    fun onPropsReady() {
        controller_name.text = name
        controller_state.text = state
    }
 }
package smarthome.client.presentation.devices.deviceaddition.epoxy

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import kotlinx.android.synthetic.main.pending_controller_item.view.*
import smarthome.client.presentation.R
import smarthome.client.presentation.controllers.controllerdetail.statechanger.extensions.inflate
import smarthome.client.presentation.visible

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class PendingControllerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    
    init {
        inflate(R.layout.pending_controller_item)
    }
    
    lateinit var name: CharSequence @TextProp set
    lateinit var state: CharSequence @TextProp set
    lateinit var type: CharSequence @TextProp set
    var isRefreshing = false @ModelProp set
    
    @AfterPropsSet
    fun onPropsReady() {
        name_text.text = name.takeUnless { it.isEmpty() } ?: "Empty name"
        state_text.text = state
        type_text.text = type
        progress.visible = isRefreshing
    }
}

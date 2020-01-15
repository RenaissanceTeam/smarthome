package smarthome.client.presentation.home.epoxy

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import kotlinx.android.synthetic.main.device_item.view.*
import smarthome.client.presentation.R
import smarthome.client.presentation.controllers.controllerdetail.statechanger.extensions.inflate

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class DeviceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    
    init {
        inflate(R.layout.device_item)
    }
    
    lateinit var name: CharSequence @TextProp set
    lateinit var type: CharSequence @TextProp set
    var onClick: (() -> Unit)? = null @CallbackProp set
    
    
    @AfterPropsSet
    fun onPropsReady() {
        device_name.text = name
        device_type.text = type
        
        rootView.setOnClickListener { onClick?.invoke() }
    }
}


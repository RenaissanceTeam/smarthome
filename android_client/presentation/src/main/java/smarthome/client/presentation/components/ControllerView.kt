package smarthome.client.presentation.components

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.airbnb.epoxy.*
import kotlinx.android.synthetic.main.controller_item.view.*
import smarthome.client.presentation.R
import smarthome.client.presentation.util.inflate
import smarthome.client.presentation.visible

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class ControllerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    init {
        inflate(R.layout.controller_item)
    }
    
    lateinit var name: CharSequence @TextProp set
    lateinit var type: CharSequence @TextProp set
    lateinit var state: CharSequence @TextProp set
    var isRefreshing = false @ModelProp set
    var onClick: (() -> Unit)? = null @CallbackProp set
    var onLongClick: (() -> Unit)? = null @CallbackProp set
    
    
    @AfterPropsSet
    fun onPropsReady() {
        controller_name.text = name
        controller_type.text = type
        controller_state.text = state
        progress.visible = isRefreshing
        
        rootView.setOnClickListener { onClick?.invoke() }
        rootView.setOnLongClickListener { onLongClick?.invoke(); true}
    }
}
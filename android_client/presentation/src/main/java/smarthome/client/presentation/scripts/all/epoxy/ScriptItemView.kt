package smarthome.client.presentation.scripts.all.epoxy

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.airbnb.epoxy.*
import kotlinx.android.synthetic.main.script_item.view.*
import smarthome.client.entity.NOT_DEFINED_ID
import smarthome.client.presentation.R
import smarthome.client.presentation.util.inflate

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class ScriptItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        inflate(R.layout.script_item)
    }

    lateinit var name: CharSequence @TextProp set
    lateinit var description: CharSequence @TextProp set
    var onClick: (() -> Unit)? = null @CallbackProp set
    var onEnableClick: ((Boolean) -> Unit)? = null @CallbackProp set
    var scriptEnabled: Boolean = false @ModelProp set

    @AfterPropsSet
    fun onPropsReady() {
        script_name.text = name
        script_description.text = description
        script_enabled.isChecked = scriptEnabled

        script_enabled.setOnCheckedChangeListener { _, isChecked ->
            onEnableClick?.invoke(isChecked)
        }

        setOnClickListener { onClick?.invoke() }
    }
}
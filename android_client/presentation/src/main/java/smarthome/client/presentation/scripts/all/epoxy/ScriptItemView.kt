package smarthome.client.presentation.scripts.all.epoxy

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.airbnb.epoxy.*
import kotlinx.android.synthetic.main.script_item.view.*
import smarthome.client.presentation.R
import smarthome.client.presentation.util.inflate
import smarthome.client.util.fadeVisibility
import smarthome.client.util.fold
import smarthome.client.util.visible

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class ScriptItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        inflate(R.layout.script_item)
    }

    var scriptId: Long = 0 @ModelProp set
    lateinit var name: CharSequence @TextProp set
    lateinit var description: CharSequence @TextProp set
    var onClick: (() -> Unit)? = null @CallbackProp set
    var onEnableClick: ((Boolean) -> Unit)? = null @CallbackProp set
    var scriptEnabled: Boolean = false @ModelProp set
    var enableInProgress: Boolean = false @ModelProp set

    @AfterPropsSet
    fun onPropsReady() {

        name.isEmpty().fold(
                ifTrue = {
                    script_name.text = "Empty name"
                    script_name.setTextAppearance(R.style.EmptyTitleTextStyle)
                },
                ifFalse = {
                    script_name.text = name
                    script_name.setTextAppearance(R.style.TitleTextStyle)
                }
        )

        script_description.text = description
        script_description.visible = description.isNotEmpty()

        script_enabled.fadeVisibility(!enableInProgress)
        script_enabled_progress.fadeVisibility(enableInProgress)

        script_enabled.setOnCheckedChangeListener(null)
        script_enabled.isChecked = scriptEnabled
        script_enabled.setOnCheckedChangeListener { _, isChecked ->
            onEnableClick?.invoke(isChecked)
        }


        setOnClickListener { onClick?.invoke() }
    }
}
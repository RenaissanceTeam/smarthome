package smarthome.client.arduino.scripts.presentation.onoff

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import kotlinx.android.synthetic.main.digital_condition_view.view.*
import smarthome.client.arduino.scripts.entity.action.getStateFromBoolean
import smarthome.client.arduino.scripts.entity.action.on
import smarthome.client.arduino_plugin.R
import smarthome.client.presentation.scripts.setup.dependency.DependencyUnitView
import smarthome.client.presentation.util.inflate

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class OnOffConditionView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : DependencyUnitView(context, attrs, defStyleAttr) {

    override fun onCreateView(viewGroup: ViewGroup) {
        viewGroup.inflate(R.layout.onoff_condition_view)
    }

    var value: String = on @ModelProp set
    var onChange: ((String) -> Unit)? = null @CallbackProp set

    @AfterPropsSet
    fun onPropsReady() {
        checkbox.setOnCheckedChangeListener { _, isChecked ->
            onChange?.invoke(getStateFromBoolean(isChecked))
        }

        if (!checkbox.hasFocus()) checkbox.isChecked = value == on
    }
}
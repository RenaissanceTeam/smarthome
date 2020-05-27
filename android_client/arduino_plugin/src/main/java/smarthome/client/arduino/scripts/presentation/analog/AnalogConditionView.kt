package smarthome.client.arduino.scripts.presentation.analog

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.SeekBar
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import kotlinx.android.synthetic.main.analog_condition_view.view.*
import smarthome.client.arduino_plugin.R
import smarthome.client.presentation.scripts.setup.dependency.DependencyUnitView
import smarthome.client.presentation.util.extensions.disallowInterceptTouchEvent
import smarthome.client.presentation.util.extensions.setOnChangeListener
import smarthome.client.presentation.util.inflate

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class AnalogConditionView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : DependencyUnitView(context, attrs, defStyleAttr) {

    override fun onCreateView(viewGroup: ViewGroup) {
        viewGroup.inflate(R.layout.analog_condition_view)

        seekbar.disallowInterceptTouchEvent()
    }

    var value: Int = 0 @ModelProp set
    var onChange: ((Int) -> Unit)? = null @CallbackProp set

    @AfterPropsSet
    fun onPropsReady() {
        seekbar.setOnChangeListener {
            onChange?.invoke(it)
        }

        if (!seekbar.hasFocus()) seekbar.progress = value
    }

 }
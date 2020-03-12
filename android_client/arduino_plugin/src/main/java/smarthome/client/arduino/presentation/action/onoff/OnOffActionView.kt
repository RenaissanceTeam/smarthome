package smarthome.client.arduino.presentation.action.onoff

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import kotlinx.android.synthetic.main.onoff_action_view.view.*
import smarthome.client.arduino.entity.action.off
import smarthome.client.arduino.entity.action.on
import smarthome.client.arduino_plugin.R
import smarthome.client.presentation.scripts.addition.dependency.DependencyUnitView
import smarthome.client.presentation.util.inflate
import smarthome.client.util.invisible
import smarthome.client.util.show

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class OnOffActionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : DependencyUnitView(context, attrs, defStyleAttr) {
    
    override fun onCreateView(viewGroup: ViewGroup) {
        viewGroup.inflate(R.layout.onoff_action_view)
    }
    
    @ModelProp
    lateinit var state: String
    var onChangeState: ((String) -> Unit)? = null @CallbackProp set
    
    @AfterPropsSet
    fun onPropsReady() {
        onoff_switch.setOnCheckedChangeListener { _, isChecked ->
            val newState = booleanToState(isChecked)
            bindStateLabels(newState)
    
            onChangeState?.invoke(newState)
        }
    
        if (!onoff_switch.hasFocus()) onoff_switch.isChecked = state == on
        bindStateLabels(state)
    }
    
    private fun bindStateLabels(state: String) {
        when (state) {
            on -> turnOnLabels()
            off -> turnOffLabels()
        }
    }
    
    private fun turnOnLabels() {
        turn_on.show()
        turn_off.invisible()
    }
    
    private fun turnOffLabels() {
        turn_on.invisible()
        turn_off.show()
    }
    
    private fun booleanToState(bool: Boolean): String {
        return when (bool) {
            true -> on
            else -> off
        }
    }
}
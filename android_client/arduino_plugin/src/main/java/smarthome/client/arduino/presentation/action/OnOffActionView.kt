package smarthome.client.arduino.presentation.action

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.airbnb.epoxy.ModelView
import kotlinx.android.synthetic.main.onoff_action_view.view.*
import smarthome.client.arduino.entity.action.OnOffActionData
import smarthome.client.arduino.entity.action.getStateFromBoolean
import smarthome.client.arduino_plugin.R
import smarthome.client.entity.script.dependency.action.Action
import smarthome.client.presentation.scripts.addition.dependency.action.ActionView
import smarthome.client.presentation.util.inflate
import smarthome.client.util.invisible
import smarthome.client.util.show

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class OnOffActionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), ActionView {
    private var action: Action? = null
    init {
        inflate(R.layout.onoff_action_view)
        
        onoff_switch.setOnCheckedChangeListener { _, isChecked ->
//            action?.let {
//                action = it.copy(value = getStateFromBoolean(isChecked))
//            }
//
//            when (isChecked) {
//                true -> {
//                    turn_on.show()
//                    turn_off.invisible()
//                }
//                false -> {
//                    turn_on.invisible()
//                    turn_off.show()
//                }
//            }
            // todo
        }
    }
    
    override fun setAction(action: Action) {
        if (action.data is OnOffActionData) this.action = action
    }
    
    override fun getAction(): Action {
        return action ?: throw IllegalStateException("No action has been set for $this")
    }
}
package smarthome.client.arduino.presentation.action

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.onoff_action_view.view.*
import smarthome.client.arduino_plugin.R
import smarthome.client.presentation.scripts.addition.dependency.action.ActionView
import smarthome.client.presentation.util.inflate
import smarthome.client.util.invisible
import smarthome.client.util.show

class OnOffActionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), ActionView {
    
    init {
        inflate(R.layout.onoff_action_view)
        
        onoff_switch.setOnCheckedChangeListener { _, isChecked ->
            when (isChecked) {
                true -> {
                    turn_on.show()
                    turn_off.invisible()
                }
                false -> {
                    turn_on.invisible()
                    turn_off.show()
                }
            }
        }
    }
    
}
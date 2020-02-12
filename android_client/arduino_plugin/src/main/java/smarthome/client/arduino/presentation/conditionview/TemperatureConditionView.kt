package smarthome.client.arduino.presentation.conditionview

import android.content.Context
import android.util.AttributeSet
import com.airbnb.epoxy.ModelView
import smarthome.client.presentation.scripts.addition.dependency.condition.ControllerConditionValueView

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class TemperatureConditionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ControllerConditionValueView(context, attrs, defStyleAttr) {
    
    init {
        title = "Temperature"
    }
}
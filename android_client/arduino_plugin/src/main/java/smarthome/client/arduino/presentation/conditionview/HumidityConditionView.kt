package smarthome.client.arduino.presentation.conditionview

import android.content.Context
import android.util.AttributeSet
import smarthome.client.presentation.scripts.addition.dependency.condition.ConditionValueView

class HumidityConditionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConditionValueView(context, attrs, defStyleAttr) {
    
    init {
        title = "Humidity"
    }
}
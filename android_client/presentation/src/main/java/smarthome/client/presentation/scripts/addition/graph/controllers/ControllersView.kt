package smarthome.client.presentation.scripts.addition

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import smarthome.client.presentation.R
import smarthome.client.presentation.util.inflate

class ControllersView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    
    init {
        inflate(R.layout.scripts_controllers_to_add)
    }
}
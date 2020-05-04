package smarthome.client.presentation.scripts.setup.dependency.condition.time

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import com.airbnb.epoxy.*
import smarthome.client.presentation.R
import smarthome.client.presentation.scripts.setup.dependency.DependencyUnitView
import smarthome.client.presentation.util.inflate

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class TimerConditionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : DependencyUnitView(context, attrs, defStyleAttr) {

    override fun onCreateView(viewGroup: ViewGroup) {
        viewGroup.inflate(R.layout.scripts_timer_condition)
    }
 }
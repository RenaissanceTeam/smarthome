package smarthome.client.presentation.scripts.setup.dependency.condition.time

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import kotlinx.android.synthetic.main.scripts_weekdays_condition.view.*
import org.joda.time.LocalTime
import smarthome.client.presentation.R
import smarthome.client.presentation.scripts.setup.dependency.DependencyUnitView
import smarthome.client.presentation.util.inflate
import smarthome.client.util.log

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class WeekdaysConditionView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : DependencyUnitView(context, attrs, defStyleAttr) {

    override fun onCreateView(viewGroup: ViewGroup) {
        viewGroup.inflate(R.layout.scripts_weekdays_condition)
    }

    var onChangeTime: ((LocalTime) -> Unit)? = null @CallbackProp set
    var onChangeSelected: ((List<Int>) -> Unit)? = null @CallbackProp set

    @ModelProp
    lateinit var time: LocalTime

    @ModelProp
    lateinit var days: List<Int>

    @AfterPropsSet
    fun onPropsReady() {
//        weekday_selector.setSelected(days)
//        weekday_selector.onChangeSelected {
//            this.onChangeSelected?.invoke(it)
//        }
//
//        if (!timepicker.hasFocus()) timepicker.hour = time.hourOfDay
//        if (!timepicker.hasFocus()) timepicker.minute = time.minuteOfHour
//        timepicker.setOnTimeChangedListener { view, hourOfDay, minute ->
//            onChangeTime?.invoke(LocalTime(hourOfDay, minute))
//        }
    }
}
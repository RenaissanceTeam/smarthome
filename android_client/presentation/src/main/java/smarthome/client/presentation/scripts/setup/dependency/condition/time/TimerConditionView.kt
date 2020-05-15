package smarthome.client.presentation.scripts.setup.dependency.condition.time

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import kotlinx.android.synthetic.main.scripts_timer_condition.view.*
import smarthome.client.presentation.R
import smarthome.client.presentation.scripts.setup.dependency.DependencyUnitView
import smarthome.client.presentation.util.extensions.disallowInterceptTouchEvent
import smarthome.client.presentation.util.extensions.setOnChangeListener
import smarthome.client.presentation.util.inflate

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class TimerConditionView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : DependencyUnitView(context, attrs, defStyleAttr) {

    private val factors = mapOf(
            25 to 5,
            31 to 10,
            37 to 60,
            45 to 60*5,
            55 to 60 * 10,
            MAX_PROGRESS to 60 * 30
    )
    private val calculatedProgress: MutableList<Int> = (0..MAX_PROGRESS).toMutableList()

    init {
        var currentSum = MIN_SECONDS
        var currentFactor = 1

        for (i in (0..MAX_PROGRESS)) {
            factors[i]?.let { currentFactor = it }
            currentSum += currentFactor

            calculatedProgress[i] = currentSum
        }
    }

    override fun onCreateView(viewGroup: ViewGroup) {
        viewGroup.inflate(R.layout.scripts_timer_condition)

        timer_seekbar.max = MAX_PROGRESS
        timer_seekbar.disallowInterceptTouchEvent()
    }

    var timer: Int = 0 @ModelProp set
    var onChangeTimer: ((Int) -> Unit)? = null @CallbackProp set


    @AfterPropsSet
    fun onPropsReady() {
        timer_text.text = composeText(timer)
        if (!timer_seekbar.hasFocus()) timer_seekbar.progress = calculatedProgress.indexOfLast { it <= timer }

        timer_seekbar.setOnChangeListener { onChangeTimer?.invoke(calculatedProgress[it]) }
    }

    private fun composeText(time: Int): String {
        var remainingTime = time
        var result = ""
        if (remainingTime > 60 * 60) {
            val hours = remainingTime / (60 * 60)
            if (hours != 0) {
                remainingTime -= hours * 60 * 60
                result += "$hours h."
            }
        }
        if (remainingTime > 60) {
            val minutes = remainingTime / 60
            if (minutes != 0) {
                remainingTime -= minutes * 60
                result += "$minutes m."
            }
        }
        if (remainingTime > 0 && remainingTime != 0) {
            result += "$remainingTime s."
        }
        return result
    }

    companion object {
        val MIN_SECONDS = 5
        val MAX_PROGRESS = 100
    }
}

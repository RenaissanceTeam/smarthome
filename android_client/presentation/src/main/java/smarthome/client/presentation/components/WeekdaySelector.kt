package smarthome.client.presentation.components

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.children
import kotlinx.android.synthetic.main.weekday_selector.view.*
import smarthome.client.presentation.R
import smarthome.client.presentation.util.inflate
import smarthome.client.util.log

class WeekdaySelector @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val defaultSelected = false
    private val disabledColor = context.resources.getColor(R.color.lightGray, null)
    private val enabledColor = context.resources.getColor(R.color.colorAccent, null)
    private var changeSelected: ((List<Int>) -> Unit)? = null
    private val selected = mutableMapOf<Int, Boolean>()
    private val dayViews = mutableMapOf<Int, TextView>()
    private val allDaysKeys = (0..6).toList()

    init {
        inflate(R.layout.weekday_selector)
    }

    fun onChangeSelected(listener: (List<Int>) -> Unit) {
        changeSelected = listener
    }

    fun setSelected(selected: List<Int>) {
        allDaysKeys.forEach { this.selected[it] = (it in selected) }
        setOnClickListeners()

        this.dayViews.keys.forEach { day ->
            animateToSelection(day, day in selected)
        }
    }

    private fun setOnClickListeners() {
        labels_container.children.forEachIndexed { i, view ->
            if (view !is TextView) return@forEachIndexed

            view.setOnClickListener {
                selected[i] = !(selected[i] ?: defaultSelected)
                changeSelected?.invoke(selected.keys.toList().filter {
                    selected[it] ?: defaultSelected
                })
            }
            dayViews[i] = view
        }
    }

    private fun animateToSelection(day: Int, selected: Boolean) {
        dayViews[day]?.let { view ->
            val from = view.textColors.defaultColor
            val to = if (selected) enabledColor else disabledColor

            ValueAnimator.ofObject(ArgbEvaluator(), from, to)
                    .apply {
                        addUpdateListener { animator ->
                            (animator.animatedValue as? Int)?.let {
                                view.setTextColor(it)
                            }
                        }
                    }
                    .setDuration(400)
                    .start()
        }
    }
}
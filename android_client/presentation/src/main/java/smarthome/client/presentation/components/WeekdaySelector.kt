package smarthome.client.presentation.components

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.children
import kotlinx.android.synthetic.main.weekday_selector.view.*
import smarthome.client.presentation.R
import smarthome.client.presentation.util.inflate

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
    private val dayViews = mutableMapOf<Int, View>()

    init {

        inflate(R.layout.weekday_selector)
        labels_container.children.forEachIndexed { i, view ->
            view.setOnClickListener {
                selected[i] = !(selected[i] ?: defaultSelected)
                changeSelected?.invoke(selected.keys.toList().filter {
                    selected[it] ?: defaultSelected
                })
            }
            dayViews[i] = view
        }
    }

    fun onChangeSelected(listener: (List<Int>) -> Unit) {
        changeSelected = listener
    }

    fun setSelected(selected: List<Int>) {
        this.selected.keys.forEach { day ->
            animateToSelection(day, day in selected)
        }
    }

    private fun animateToSelection(day: Int, selected: Boolean) {
        val from = if (selected) enabledColor else disabledColor
        val to = if (selected) disabledColor else enabledColor

        ValueAnimator.ofObject(ArgbEvaluator(), from, to)
                .apply {
                    addUpdateListener { animator ->
                        dayViews[day]?.let { view ->
                            (animator.animatedValue as? Int)?.let {
                                (view as? TextView)?.setTextColor(it)
                            }
                        }
                    }
                }
                .setDuration(300)
                .start()
    }
}
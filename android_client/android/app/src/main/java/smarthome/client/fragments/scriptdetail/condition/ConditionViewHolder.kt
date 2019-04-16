package smarthome.client.fragments.scriptdetail.condition

import android.view.View
import android.widget.FrameLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.recyclerview.widget.RecyclerView
import smarthome.client.R
import smarthome.client.scripts.conditions.Condition

class ConditionViewHolder(view: View,
                          onTypeChange: (Int, String) -> Unit) : RecyclerView.ViewHolder(view) {
    private val fieldLayout = view.findViewById<FrameLayout>(R.id.field)
    private val type = view.findViewById<RadioGroup>(R.id.type_radio_group)
    private var boundPosition: Int = -1
    private var isBinding = false

    init {
        type.setOnCheckedChangeListener { group, checkedId ->
            if (isBinding) return@setOnCheckedChangeListener

            val button = group.findViewById<RadioButton>(checkedId)
            button ?: return@setOnCheckedChangeListener

            onTypeChange(boundPosition, button.tag.toString())
        }
    }

    fun bind(condition: Condition, position: Int) {
        isBinding = true
        boundPosition = position

        selectRadioButton(condition)
        inflateFields(condition)

        isBinding = false
    }

    private fun inflateFields(condition: Condition) {
        fieldLayout.removeAllViews()
        fieldLayout.addView(condition.getView(fieldLayout))
    }

    private fun selectRadioButton(condition: Condition) {
        val shouldBeChecked = type.findViewWithTag<RadioButton>(condition.getTag())
        if (shouldBeChecked.id != type.checkedRadioButtonId) {
            type.check(shouldBeChecked.id)
        }
    }
}
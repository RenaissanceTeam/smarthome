package smarthome.client.fragments.scriptdetail.action

import android.view.View
import android.widget.FrameLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.recyclerview.widget.RecyclerView
import smarthome.client.R
import smarthome.client.scripts.actions.Action
import smarthome.client.scripts.actions.ActionViewBuilder
import smarthome.client.scripts.conditions.Condition

class ActionViewHolder(view: View,
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

    fun bind(action: Action, position: Int) {
        isBinding = true
        boundPosition = position

        selectRadioButton(action)
        if (action is ActionViewBuilder) inflateFields(action)

        isBinding = false
    }

    private fun inflateFields(action: ActionViewBuilder) {
        fieldLayout.removeAllViews()
        fieldLayout.addView(action.getView(fieldLayout))
    }

    private fun selectRadioButton(action: Action) {
        val shouldBeChecked = type.findViewWithTag<RadioButton>(action.getTag())
        if (shouldBeChecked.id != type.checkedRadioButtonId) {
            type.check(shouldBeChecked.id)
        }
    }
}
package smarthome.client.presentation.screens.scripts.conditions

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import smarthome.client.CONDITION_EXACT_TIME
import smarthome.client.R
import smarthome.library.common.scripts.conditions.ExactTimeCondition

class ExactTimeConditionViewWrapper(private val condition: ExactTimeCondition): ConditionViewWrapper {

    override fun getTag() = CONDITION_EXACT_TIME

    override fun getView(root: ViewGroup): View {
        val view = inflateLayout(root, R.layout.field_text_input)

        view.findViewById<TextView>(R.id.field_before).text = "At time"
        val input = view.findViewById<EditText>(R.id.field_input)
        input.setText(condition.state)

        input.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                condition.state = s?.toString() ?: ""
            }

            override fun beforeTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
            override fun onTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
        })
        return view
    }

    override fun isFilled(): Boolean {
        val isFilled = !condition.state.isEmpty()
        if (!isFilled) {
            // todo highlight
        }
        return isFilled
    }

    override fun toString() = condition.toString()

}
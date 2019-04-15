package smarthome.client.scripts.conditions

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import smarthome.client.CONDITION_EXACT_TIME
import smarthome.client.R

class ExactTimeCondition : Condition() {
    var state = ""

    override fun getTag() = CONDITION_EXACT_TIME

    override fun getView(root: ViewGroup): View {
        val view = inflateLayout(root, R.layout.field_text_input)

        view.findViewById<TextView>(R.id.field_before).text = "At time"
        val input = view.findViewById<EditText>(R.id.field_input)
        input.setText(state)

        input.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                state = s?.toString() ?: ""
            }

            override fun beforeTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
            override fun onTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
        })
        return view
    }

    override fun toString() = "At time $state"

    override fun evaluate(): Boolean = true // todo
}
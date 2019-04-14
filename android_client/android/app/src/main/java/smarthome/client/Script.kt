package smarthome.client

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView

class Script(val name: String,
             val conditions: MutableList<Condition>,
             val actions: MutableList<Action>)

abstract class Condition {
    companion object {
        fun withTitle(title: String): Condition {
            return when (title) {
                "Controller" -> ControllerCondition()
                "Exact Time" -> ExactTimeCondition()
                else -> throw RuntimeException("No condition with title $title")
            }
        }
    }

    abstract fun getTag(): String
    abstract fun evaluate(): Boolean
    abstract fun getFields(): List<ConditionField>
}




class ControllerCondition: Condition() {
    override fun getTag() = "Controller"

    private val controllerField = TextConditionField("Controller")
    private val fields = listOf(controllerField)

    override fun getFields() = fields

    override fun evaluate(): Boolean = true

    override fun toString() = fields.joinToString(separator = " and ")
}

class ExactTimeCondition: Condition() {
    override fun getTag() = "Exact Time"

    private val atTimeField = TextConditionField("At time")
    private val fields = listOf(atTimeField)

    override fun getFields() = fields

    override fun evaluate(): Boolean = true

    override fun toString() = fields.joinToString(separator = " and ")
}

abstract class ConditionField {
    var state: String = ""
    abstract fun getView(root: ViewGroup): View
}

class TextConditionField(private val before: String,
                         private val after: String = ""): ConditionField() {
    override fun getView(root: ViewGroup): View {
        val inflater = LayoutInflater.from(root.context)
        val view = inflater.inflate(R.layout.field_text_input, root, false)
        view.findViewById<TextView>(R.id.field_before).text = before
        view.findViewById<TextView>(R.id.field_after).text = after
        val input = view.findViewById<EditText>(R.id.field_input)
        input.setText(state)

        input.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                state = s?.toString() ?: ""
            }
            override fun beforeTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
            override fun onTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
        })
        return view
    }

    override fun toString() = "$before = $state $after"
}

abstract class Action {
    abstract fun action()
}

class MockAction: Action() {
    override fun action() {}
    override fun toString() = "Turn on 'GarageLight'"
}


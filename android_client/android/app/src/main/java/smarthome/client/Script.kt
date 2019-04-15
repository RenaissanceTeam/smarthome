package smarthome.client

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatSpinner
import smarthome.library.common.BaseController

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


class ControllerCondition(private val controllers: List<BaseController>) : Condition() {
    override fun getTag() = "Controller"

    private val controllerField = ControllerConditionField(controllers)
    private val fields = listOf(controllerField)

    override fun getFields() = fields

    override fun evaluate(): Boolean = true

    override fun toString() = fields.joinToString(separator = " and ")
}

class ExactTimeCondition : Condition() {
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

    internal fun inflateLayout(root: ViewGroup, layout: Int): View {
        val inflater = LayoutInflater.from(root.context)
        return inflater.inflate(R.layout.field_text_input, root, false)
    }
}

class TextConditionField(private val before: String,
                         private val after: String = "") : ConditionField() {

    override fun getView(root: ViewGroup): View {
        val view = inflateLayout(root, R.layout.field_text_input)

        view.findViewById<TextView>(R.id.field_before).text = before
        view.findViewById<TextView>(R.id.field_after).text = after
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

    override fun toString() = "$before = $state $after"
}

class ControllerConditionField(private val controllers: List<BaseController>) : ConditionField() {

    private val controllersWithName = controllers.filter { it.name != null }.map { it.name }.toTypedArray()
    var choosenController: BaseController? = null
    var compare: String? = null
    var value: String? = null

    override fun getView(root: ViewGroup): View {
        val view = inflateLayout(root, R.layout.field_controller)

        val radioGroup = view.findViewById<RadioGroup>(R.id.compare_radio_group)
        val dropDownList = view.findViewById<AppCompatSpinner>(R.id.controller_drop_down_list)
        val valueInput = view.findViewById<EditText>(R.id.value_input)

        dropDownList.adapter = ArrayAdapter<String>(view.context,
                android.R.layout.simple_spinner_dropdown_item, controllersWithName)
        dropDownList.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val name = controllersWithName[position]
            choosenController = controllers.find { it.name == name }
        }
        return view
    }


    override fun toString() = "Controller ${choosenController?.name} "
}


abstract class Action {
    abstract fun action()
}

class MockAction : Action() {
    override fun action() {}
    override fun toString() = "Turn on 'GarageLight'"
}


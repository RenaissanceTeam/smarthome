package smarthome.client

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatSpinner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import smarthome.client.fragments.scriptdetail.ScriptDetailViewModel
import smarthome.library.common.BaseController

class Script(val name: String,
             val conditions: MutableList<Condition>,
             val actions: MutableList<Action>)

abstract class Condition {
    companion object {
        fun withTag(tag: String, provider: AllConditionsProvider): Condition {
            return when (tag) {
                CONDITION_CONTROLLER -> ControllerCondition(provider)
                CONDITION_EXACT_TIME -> ExactTimeCondition()
                else -> throw RuntimeException("No condition with tag $tag")
            }
        }
    }
    var state: String = ""
    abstract fun getView(root: ViewGroup): View

    internal fun inflateLayout(root: ViewGroup, layout: Int): View {
        val inflater = LayoutInflater.from(root.context)
        return inflater.inflate(layout, root, false)
    }

    abstract fun getTag(): String
    abstract fun evaluate(): Boolean
}

interface AllConditionsProvider: ControllerConditionProvider

interface ControllerConditionProvider {
    suspend fun getControllers(): List<BaseController>
}

class ControllerCondition(provider: ControllerConditionProvider) : Condition() {
    var choosenController: BaseController? = null
    var compare: String? = null
    var value: String? = null
    private var controllersWithName: List<BaseController>? = null
    private var hasPendingBinding = false
    private lateinit var view: View

    init {
        CoroutineScope(Dispatchers.Main).launch {
            val controllers = provider.getControllers()
            controllersWithName = controllers.filter { it.name != null }
            if (hasPendingBinding) bindView()
        }
    }

    override fun getTag() = CONDITION_CONTROLLER

    override fun getView(root: ViewGroup): View {
        view = inflateLayout(root, R.layout.field_controller)
        bindView()
        return view
    }

    private fun bindView() {
        val radioGroup = view.findViewById<RadioGroup>(R.id.compare_radio_group)
        val dropDownList = view.findViewById<AppCompatSpinner>(R.id.controller_drop_down_list)
        val valueInput = view.findViewById<EditText>(R.id.value_input)

        val dataset = controllersWithName?.map { it.name }?.toTypedArray()

        if (dataset == null) {
            hasPendingBinding = true
            return
        }

        dropDownList.adapter = ArrayAdapter<String>(view.context,
                android.R.layout.simple_spinner_dropdown_item, dataset)

        dropDownList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                choosenController = null
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                choosenController = controllersWithName?.get(position)
            }
        }


        hasPendingBinding = false
    }

    override fun evaluate(): Boolean = true // todo

    override fun toString() = "Controller ${choosenController?.name} "
}

class ExactTimeCondition : Condition() {
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


abstract class Action {
    abstract fun action()
}

class MockAction : Action() {
    override fun action() {}
    override fun toString() = "Turn on 'GarageLight'"
}


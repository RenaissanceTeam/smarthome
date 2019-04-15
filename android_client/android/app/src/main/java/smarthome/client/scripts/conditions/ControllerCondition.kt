package smarthome.client.scripts.conditions

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.RadioGroup
import androidx.appcompat.widget.AppCompatSpinner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import smarthome.client.*
import smarthome.library.common.BaseController

class ControllerCondition(provider: ControllerConditionProvider) : Condition() {
    var chosenController: BaseController? = null
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

        setEventListeners(radioGroup, valueInput, dropDownList)

        hasPendingBinding = false
    }

    private fun setEventListeners(radioGroup: RadioGroup,
                                  valueInput: EditText,
                                  dropDownList: AppCompatSpinner) {
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            compare = when (checkedId) {
                R.id.less_than -> COMPARE_LESS_THAN
                R.id.more_than -> COMPARE_MORE_THAN
                R.id.equal_to -> COMPARE_EQUAL_TO
                else -> throw RuntimeException("Can't parse compare option")
            }
        }

        valueInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                value = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        dropDownList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                chosenController = null
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                chosenController = controllersWithName?.get(position)
            }
        }
    }

    override fun evaluate(): Boolean {
        val controller = chosenController ?: return false
        val value = value ?: return false

        return when (compare) {
            COMPARE_LESS_THAN -> controller.state.toDouble() < value.toDouble()
            COMPARE_MORE_THAN -> controller.state.toDouble() > value.toDouble()
            COMPARE_EQUAL_TO -> controller.state.toDouble() == value.toDouble()
            else -> throw RuntimeException("Unsupported compare: $compare")
        }
    }

    override fun toString() = "Controller ${chosenController?.name} $compare $value"
}
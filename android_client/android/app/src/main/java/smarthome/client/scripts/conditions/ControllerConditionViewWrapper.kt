package smarthome.client.scripts.conditions

import android.R
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
import smarthome.client.CONDITION_CONTROLLER
import smarthome.library.common.constants.*
import smarthome.client.scripts.ControllersProvider
import smarthome.library.common.scripts.conditions.ControllerCondition
import smarthome.library.common.BaseController

class ControllerConditionViewWrapper(provider: ControllersProvider, private val condition: ControllerCondition) : ConditionViewWrapper {
    private var controllersWithName: List<BaseController>? = null
    private var hasPendingBinding = false
    private lateinit var view: View

    private val compareStateToViewId = mapOf(
            COMPARE_LESS_THAN to smarthome.client.R.id.less_than,
            COMPARE_MORE_THAN to smarthome.client.R.id.more_than,
            COMPARE_EQUAL_TO to smarthome.client.R.id.equal_to)

    init {
        CoroutineScope(Dispatchers.Main).launch {
            val controllers = provider.getControllers()
            controllersWithName = controllers.filter { it.name != null }
            if (hasPendingBinding) bindView()
        }
    }

    override fun getTag() = CONDITION_CONTROLLER

    override fun getView(root: ViewGroup): View {
        view = inflateLayout(root, smarthome.client.R.layout.field_controller)
        bindView()
        return view
    }

    override fun isFilled(): Boolean {
        val isFilled = (condition.chosenController != null && condition.compare != null && condition.value != null)
        if (!isFilled) {
            // todo highlight not filled with red
        }
        return isFilled
    }


    private fun bindView() {
        val radioGroup = view.findViewById<RadioGroup>(smarthome.client.R.id.compare_radio_group)
        val dropDownList = view.findViewById<AppCompatSpinner>(smarthome.client.R.id.controller_drop_down_list)
        val valueInput = view.findViewById<EditText>(smarthome.client.R.id.value_input)

        checkSelectedRadioGroup(radioGroup)
        valueInput.setText(condition.value)

        val dataset = controllersWithName?.map { it.name }?.toTypedArray()
        if (dataset == null) {
            hasPendingBinding = true
            return
        }

        dropDownList.adapter = ArrayAdapter<String>(view.context,
                R.layout.simple_spinner_dropdown_item, dataset)

        val selectedControllerPosition = controllersWithName?.indexOf(condition.chosenController)
        selectedControllerPosition?.let {
            dropDownList.setSelection(it)
        }

        setEventListeners(radioGroup, valueInput, dropDownList)

        hasPendingBinding = false
    }

    private fun checkSelectedRadioGroup(radioGroup: RadioGroup) {
        val id = compareStateToViewId[condition.compare] ?: return
        radioGroup.check(id)
    }

    private fun setEventListeners(radioGroup: RadioGroup,
                                  valueInput: EditText,
                                  dropDownList: AppCompatSpinner) {
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            condition.compare = compareStateToViewId.asIterable()
                    .filter { it.value == checkedId }
                    .map { it.key }[0]
        }

        valueInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                condition.value = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        dropDownList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                condition.chosenController = null
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                condition.chosenController = controllersWithName?.get(position)
            }
        }
    }

    override fun toString() = condition.toString()
}
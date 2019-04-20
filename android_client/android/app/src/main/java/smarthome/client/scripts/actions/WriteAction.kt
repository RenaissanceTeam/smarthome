package smarthome.client.scripts.actions

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
import smarthome.client.scripts.ControllersProvider
import smarthome.library.common.BaseController

class WriteAction(provider: ControllersProvider) : Action() {
    var chosenController: BaseController? = null
    var value: String? = null
    var compare: String? = null
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

    override fun isFilled(): Boolean {
        return chosenController != null && !value.isNullOrEmpty()
    }

    override fun getView(root: ViewGroup): View {
        view = inflateLayout(root, R.layout.action_write_controller)
        bindView()
        return view
    }

    override fun getTag() = ACTION_WRITE_CONTROLLER

    override fun toString() = "Write $value to ${chosenController?.name}"

    private fun bindView() {
        val dropDownList = view.findViewById<AppCompatSpinner>(R.id.controller_drop_down_list)
        val valueInput = view.findViewById<EditText>(R.id.value_input)

        val dataset = controllersWithName?.map { it.name }?.toTypedArray()

        if (dataset == null) {
            hasPendingBinding = true
            return
        }

        dropDownList.adapter = ArrayAdapter<String>(view.context,
                android.R.layout.simple_spinner_dropdown_item, dataset)

        setEventListeners(valueInput, dropDownList)

        hasPendingBinding = false
    }

    private fun setEventListeners(valueInput: EditText,
                                  dropDownList: AppCompatSpinner) {

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

}
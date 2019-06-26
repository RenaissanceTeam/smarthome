package smarthome.client.presentation.screens.scripts.actions

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.appcompat.widget.AppCompatSpinner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import smarthome.client.util.ACTION_WRITE_CONTROLLER
import smarthome.client.R
import smarthome.client.presentation.screens.scripts.ControllersProvider
import smarthome.library.common.scripts.actions.WriteAction
import smarthome.library.common.BaseController

class WriteActionViewWrapper(provider: ControllersProvider, private val action:WriteAction): ActionViewWrapper {

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
        return action.chosenController != null && !action.value.isNullOrEmpty()
    }

    override fun getView(root: ViewGroup): View {
        view = inflateLayout(root, R.layout.action_write_controller)
        bindView()
        return view
    }

    override fun getTag() = ACTION_WRITE_CONTROLLER

    override fun toString() = action.toString()


    private fun bindView() {
        val dropDownList = view.findViewById<AppCompatSpinner>(R.id.controller_drop_down_list)
        val valueInput = view.findViewById<EditText>(R.id.value_input)

        valueInput.setText(action.value)

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
                action.value = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        dropDownList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                action.chosenController = null
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                action.chosenController = controllersWithName?.get(position)
            }
        }
    }

}
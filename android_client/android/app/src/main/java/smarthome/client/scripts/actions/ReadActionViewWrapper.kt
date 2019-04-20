package smarthome.client.scripts.actions

import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatSpinner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import smarthome.client.ACTION_READ_CONTROLLER
import smarthome.client.R
import smarthome.client.scripts.ControllersProvider
import smarthome.client.scripts.commonlib.scripts.actions.ReadAction
import smarthome.library.common.BaseController

class ReadActionViewWrapper(private val provider: ControllersProvider,
                            private val action: ReadAction) : ActionViewWrapper {
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
        return action.chosenController != null
    }

    override fun getView(root: ViewGroup): View {
        view = inflateLayout(root, R.layout.action_read_controller)
        bindView()
        return view
    }

    override fun getTag() = ACTION_READ_CONTROLLER

    override fun toString() = action.toString()

    private fun bindView() {
        val dropDownList = view.findViewById<AppCompatSpinner>(R.id.controller_drop_down_list)

        val dataset = controllersWithName?.map { it.name }?.toTypedArray()

        if (dataset == null) {
            hasPendingBinding = true
            return
        }


        dropDownList.adapter = ArrayAdapter<String>(view.context,
                android.R.layout.simple_spinner_dropdown_item, dataset)

        dropDownList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                action.chosenController = null
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?,
                                        position: Int, id: Long) {
                action.chosenController = controllersWithName?.get(position)
            }
        }

        hasPendingBinding = false
    }
}
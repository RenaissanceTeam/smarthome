package smarthome.client.presentation.controllers.controllerdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_controller_details.*
import org.koin.android.ext.android.inject
import smarthome.client.entity.Controller
import smarthome.client.presentation.R
import smarthome.client.presentation.controllers.controllerdetail.statechanger.ControllerStateChanger
import smarthome.client.presentation.controllers.controllerdetail.statechanger.StateChangerFactory
import smarthome.client.presentation.ui.DialogParameters
import smarthome.client.presentation.ui.EditTextDialog
import smarthome.client.presentation.util.extensions.setTextOrEmptyPlaceholder
import smarthome.client.util.visible

class ControllerDetails : Fragment() {
    private val args: ControllerDetailsArgs by navArgs()
    private val viewModel: ControllerDetailViewModel by viewModels()
    private val stateChangerFactory: StateChangerFactory by inject()
    private var stateChanger: ControllerStateChanger? = null


    private fun bindController(controller: Controller) {
        controller_name.setTextOrEmptyPlaceholder(controller.name, "Empty Name")
        controller_type.text = controller.type
        state.setTextOrEmptyPlaceholder(controller.state, "Unknown State")

        if (stateChanger == null) {
            stateChangerFactory
                    .get(controller)
                    .also { stateChanger = it }
                    .inflate(state_changer)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_controller_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycle.addObserver(viewModel)
        viewModel.setControllerId(args.controllerGuid)

        viewModel.refresh.observe(viewLifecycleOwner) { progress_bar.visible = it }
        viewModel.controller.observe(viewLifecycleOwner, ::bindController)

        controller_name?.setOnClickListener {
            createChangeControllerNameDialog(view)
        }
    }

    private fun createChangeControllerNameDialog(view: View) {
        EditTextDialog.create(view.context,
                DialogParameters(
                        "controller name",
                        currentValue = viewModel.getCurrentControllerName()) {
                    viewModel.controllerNameChanged(it)
                }
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stateChanger?.onDestroy()
        stateChanger = null
    }
}
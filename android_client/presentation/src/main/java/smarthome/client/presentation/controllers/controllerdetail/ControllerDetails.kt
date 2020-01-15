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
import smarthome.client.presentation.controllers.controllerdetail.statechanger.StateChangerFactory
import smarthome.client.presentation.ui.DialogParameters
import smarthome.client.presentation.ui.EditTextDialog
import smarthome.client.presentation.visible

class ControllerDetails : Fragment() {
    private val args: ControllerDetailsArgs by navArgs()
    private val viewModel: ControllerDetailViewModel by viewModels()
    private val stateChangerFactory: StateChangerFactory by inject()
    private var stateChangerInitialized = false
    
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        
        lifecycle.addObserver(viewModel)
        
        viewModel.refresh.observe(this) { progress_bar.visible = it }
        viewModel.controller.observe(this, ::bindController)
    }
    
    private fun bindController(controller: Controller) {
        controller_name.text = controller.name
        controller_type.text = controller.type
        state.text = controller.state
        
        if (!stateChangerInitialized) {
            stateChangerFactory.get(controller).inflate(state_changer)
            stateChangerInitialized = true
        }
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_controller_details, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        viewModel.setControllerId(args.controllerGuid)
    
        controller_name?.setOnClickListener {
            EditTextDialog.create(view.context,
                DialogParameters("controller name", currentValue = viewModel.controller.value?.name
                    ?: "") {
                    viewModel.controllerNameChanged(it)
                }
            ).show()
        }
    }
}
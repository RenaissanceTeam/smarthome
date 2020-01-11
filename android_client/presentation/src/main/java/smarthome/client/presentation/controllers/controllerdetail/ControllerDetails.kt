package smarthome.client.presentation.controllers.controllerdetail

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.controller_item.*
import kotlinx.android.synthetic.main.fragment_controller_details.*
import kotlinx.android.synthetic.main.fragment_controller_details.state
import smarthome.client.entity.Controller
import smarthome.client.entity.Device
import smarthome.client.presentation.R
import smarthome.client.presentation.controllers.controllerdetail.statechanger.ControllerStateChanger
import smarthome.client.presentation.controllers.controllerdetail.statechanger.StateChangerType
import smarthome.client.presentation.ui.DialogParameters
import smarthome.client.presentation.ui.EditTextDialog
import smarthome.client.presentation.visible

class ControllerDetails : Fragment() {
    private val args: ControllerDetailsArgs by navArgs()
    
    companion object {
        const val FRAGMENT_TAG = "ControllerDetailsFragment"
    }
    
    private val viewModel: smarthome.client.presentation.controllers.controllerdetail.ControllerDetailViewModel by viewModels()
    
    private var stateChanger: ControllerStateChanger? = null
    
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        
        
        viewModel.refresh.observe(this) { progress_bar.visible = it }
        viewModel.controller.observe(this, ::bindController)
        viewModel.stateChangerType.observe(this, ::invalidateStateChanger)
    }
    
    private fun bindController(controller: Controller) {
        controller_name.text = controller.name
        controller_type.text = controller.type
        state.text = controller.state
    }
    
    private fun invalidateStateChanger(changerType: StateChangerType) {
        val container = state_changer

//        val changer = when (changerType) {
//            StateChangerType.ONOFF -> OnOffStateChanger(container) { viewModel.newStateRequest(it, STATE_PENDING_WRITE) }
//            StateChangerType.LEXEME_ONOFF -> LexemeOnOffStateChanger(container) { viewModel.newStateRequest(it, STATE_PENDING_WRITE) }
//            StateChangerType.SIMPLE_WRITE -> SimpleWriteStateChanger(container) { viewModel.newStateRequest(it, STATE_PENDING_WRITE) }
//            StateChangerType.DIMMER -> DimmerStateChanger(container, state, { state?.text = it }, { viewModel.newStateRequest(it, STATE_PENDING_WRITE) }, {  viewModel.newStateRequest(null, STATE_PENDING_READ) } )
//            StateChangerType.RGB -> RGBStateChanger(container, state?.text.toString(), { state?.text = it }, { viewModel.newStateRequest(it, STATE_PENDING_WRITE) } )
//            StateChangerType.TEXT_READ_WRITE -> TextReadWriteStateChanger(container, { viewModel.newStateRequest(it, STATE_PENDING_WRITE) }, {  viewModel.newStateRequest(null, STATE_PENDING_READ) } )
//            StateChangerType.DIGITS_DECIMAL_READ_WRITE -> TextReadWriteStateChanger(container, { viewModel.newStateRequest(it, STATE_PENDING_WRITE) }, {  viewModel.newStateRequest(null, STATE_PENDING_READ) }, DIGITS_ONLY_TEXT_TYPE )
//            StateChangerType.ONLY_READ -> ReadStateChanger(container) { viewModel.newStateRequest(null, STATE_PENDING_READ) }
//        }
//        stateChanger = changer
//        viewModel.controller.value?.let {
//            changer.invalidateNewState(it.state, it.serveState)
//        }
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_controller_details, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        name?.setOnClickListener {
            EditTextDialog.create(view.context,
                DialogParameters("controller name", currentValue = viewModel.controller.value?.name
                    ?: "") {
                    viewModel.controllerNameChanged(it)
                }
            ).show()
        }
    }
}
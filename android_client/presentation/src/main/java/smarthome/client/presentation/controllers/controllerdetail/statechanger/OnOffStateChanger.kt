package smarthome.client.presentation.controllers.controllerdetail.statechanger

import android.view.View
import android.view.ViewGroup
import com.dd.processbutton.iml.ActionProcessButton
import kotlinx.android.synthetic.main.state_changer_onoff.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import smarthome.client.domain.api.conrollers.usecases.WriteStateToControllerUseCase
import smarthome.client.entity.Controller
import smarthome.client.presentation.R
import smarthome.client.presentation.controllers.controllerdetail.statechanger.extensions.error
import smarthome.client.presentation.controllers.controllerdetail.statechanger.extensions.idle
import smarthome.client.presentation.controllers.controllerdetail.statechanger.extensions.inflate
import smarthome.client.presentation.controllers.controllerdetail.statechanger.extensions.loading

private const val on = "on"
private const val off = "off"

class OnOffStateChanger(
    private val controller: Controller,
    private val writeStateToControllerUseCase: WriteStateToControllerUseCase
) : ControllerStateChanger {
    private var currentState = controller.state
    
    private val uiScope = CoroutineScope(Dispatchers.Main)
    
    override fun inflate(container: ViewGroup) {
        container.inflate(R.layout.state_changer_onoff).also { onViewCreated(it) }
    }
    
    fun onViewCreated(rootView: View) {
        changeNormalText(rootView.change_button)
    
        rootView.change_button.setOnClickListener {
            uiScope.launch {
                val button = rootView.change_button
                
                changeLoadingText(button)
                val wantedState = getOppositeState()
                button.loading()
                
                writeStateToControllerUseCase.runCatching { execute(controller.id, wantedState) }
                    .onFailure { button.error() }
                    .onSuccess {
                        currentState = it
                        button.idle()
                        changeNormalText(button)
                    }
            }
        }
    }
    
    private fun getOppositeState() =
        when (currentState) {
            on -> off
            else -> on
        }
    
    private fun changeLoadingText(button: ActionProcessButton) {
        if (currentState == on) {
            button.loadingText = "Switching off"
        } else {
            button.loadingText = "Switching on"
        }
    }
    
    private fun changeNormalText(button: ActionProcessButton) {
        if (currentState == off) {
            button.normalText = "Switch on"
        } else {
            button.normalText = "Switch off"
        }
    }
}
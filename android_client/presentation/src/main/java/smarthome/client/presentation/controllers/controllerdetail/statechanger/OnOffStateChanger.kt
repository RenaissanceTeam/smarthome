package smarthome.client.presentation.controllers.controllerdetail.statechanger

import android.view.ViewGroup
import com.dd.processbutton.iml.ActionProcessButton
import smarthome.client.presentation.R

class OnOffStateChanger(container: ViewGroup, listener: (String) -> Unit) :
    ControllerStateChanger(container) {
    
    private val normalProgress = 0
    private val loadingProgress = 50
    private val completeProgress = 100
    private val errorProgress = -1
    private val unknownState = "Unknown"
    
    override val layout: Int
        get() = R.layout.state_changer_onoff
    
    private var currentState = "0"
    private val button = rootView.findViewById<ActionProcessButton>(R.id.change_button)
    
    init {
        button.setOnClickListener {
            changeStateToOpposite()
            changeLoadingText()
            listener(currentState)
        }
    }
    
    private fun changeStateToOpposite() {
        currentState = if (currentState == "0" || currentState == unknownState) "1" else "0"
    }
    
    private fun changeLoadingText() {
        if (currentState == "0") {
            button.loadingText = "Switching off"
        } else {
            button.loadingText = "Switching on"
        }
    }
    
    
    override fun invalidateNewState(state: String) {
        button.progress = normalProgress
        currentState = state
        changeNormalText()
    }
    
    private fun changeNormalText() {
        if (currentState == "0" || currentState == unknownState) {
            button.normalText = "Switch on"
        } else {
            button.normalText = "Switch off"
        }
    }
}
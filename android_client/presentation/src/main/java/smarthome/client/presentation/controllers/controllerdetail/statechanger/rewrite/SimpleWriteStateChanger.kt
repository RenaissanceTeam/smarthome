//package smarthome.client.presentation.controllers.controllerdetail.statechanger
//
//import android.view.ViewGroup
//import com.dd.processbutton.iml.ActionProcessButton
//import smarthome.client.presentation.R
//
//class SimpleWriteStateChanger(container: ViewGroup, listener: (String) -> Unit) :
//    ControllerStateChanger(container) {
//
//    private val normalProgress = 0
//    private val loadingProgress = 50
//
//    override val layout: Int
//        get() = R.layout.state_changer_onoff
//
//    private var currentState = ""
//    private val button = rootView.findViewById<ActionProcessButton>(R.id.change_button)
//
//    init {
//        button.normalText = WRITE_DEFAULT_TITLE
//        button.setOnClickListener {
//            listener(currentState)
//        }
//    }
//
//    override fun invalidateNewState(state: String) {
//        button.progress = normalProgress
//    }
//}
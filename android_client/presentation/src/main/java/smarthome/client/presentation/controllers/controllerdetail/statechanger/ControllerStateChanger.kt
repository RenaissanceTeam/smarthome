package smarthome.client.presentation.controllers.controllerdetail.statechanger

import android.view.View
import android.view.ViewGroup

const val WRITE_DEFAULT_TITLE = "WRITE"

abstract class ControllerStateChanger(container: ViewGroup) {
    abstract val layout: Int
    val rootView: View
    
    init {
        container.removeAllViews()
        rootView = View.inflate(container.context, layout, container)
    }
    
    abstract fun invalidateNewState(state: String)
}
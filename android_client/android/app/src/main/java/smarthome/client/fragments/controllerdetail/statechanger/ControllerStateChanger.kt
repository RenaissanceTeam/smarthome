package smarthome.client.fragments.controllerdetail.statechanger

import android.view.View
import android.view.ViewGroup

abstract class ControllerStateChanger(container: ViewGroup) {
    abstract val layout: Int
    val rootView: View
    init {
        container.removeAllViews()
        rootView = View.inflate(container.context, layout, container)
    }

    abstract fun invalidateNewState(state: String)
}
package smarthome.client.fragments.controllerdetail.statechanger

import android.view.ViewGroup
import android.widget.Button
import smarthome.client.R

class ReadStateChanger(container: ViewGroup, listener: () -> Unit):
        ControllerStateChanger(container)  {

    override val layout: Int
        get() = R.layout.state_changer_read

    override fun invalidateNewState(state: String) {}

    init {
        rootView.findViewById<Button>(R.id.read).setOnClickListener { listener() }
    }

}

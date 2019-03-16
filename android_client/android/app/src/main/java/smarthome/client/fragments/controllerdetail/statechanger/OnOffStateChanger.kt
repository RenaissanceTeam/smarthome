package smarthome.client.fragments.controllerdetail.statechanger

import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import smarthome.client.R

class OnOffStateChanger(container: ViewGroup, listener: (String) -> Unit):
        ControllerStateChanger(container) {

    override val layout: Int
        get() = R.layout.state_changer_onoff

    val switchCompat = rootView.findViewById<SwitchCompat>(R.id.controller_switch)

    init {
        switchCompat.setOnClickListener {
            listener(clickToState())
        }
    }

    private fun clickToState(): String = if (switchCompat.isChecked) "1" else "0"

    override fun invalidateNewState(state: String) {
        switchCompat.isChecked = "1" == state
    }
}
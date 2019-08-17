package smarthome.client.presentation.fragments.controllerdetail.statechanger

import android.view.ViewGroup
import com.dd.processbutton.iml.ActionProcessButton
import smarthome.client.presentation.R

class ReadStateChanger(container: ViewGroup, listener: () -> Unit) :
        ControllerStateChanger(container) {

    private val normalProgress = 0
    private val loadingProgress = 50
    private val completeProgress = 100
    private val errorProgress = -1

    override val layout: Int
        get() = R.layout.state_changer_read

    private val button: ActionProcessButton = rootView.findViewById(R.id.read)

    override fun invalidateNewState(state: String?, serveState: String?) {
        if (serveState == "up to date" || serveState == null) button.progress = normalProgress
        else button.progress = loadingProgress
    }

    init {
        button.setMode(ActionProcessButton.Mode.ENDLESS)

        button.setOnClickListener {
            listener()
            button.progress = loadingProgress
        }

    }

}

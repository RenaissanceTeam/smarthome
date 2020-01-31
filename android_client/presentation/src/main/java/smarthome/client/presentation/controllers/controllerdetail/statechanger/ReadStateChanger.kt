package smarthome.client.presentation.controllers.controllerdetail.statechanger

import android.view.View
import android.view.ViewGroup
import com.dd.processbutton.iml.ActionProcessButton
import kotlinx.android.synthetic.main.state_changer_read.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import smarthome.client.domain.api.conrollers.usecases.ReadControllerUseCase
import smarthome.client.entity.Controller
import smarthome.client.presentation.R
import smarthome.client.presentation.controllers.controllerdetail.statechanger.extensions.error
import smarthome.client.presentation.controllers.controllerdetail.statechanger.extensions.idle
import smarthome.client.presentation.controllers.controllerdetail.statechanger.extensions.inflate
import smarthome.client.presentation.controllers.controllerdetail.statechanger.extensions.loading


class ReadStateChanger(
    private val id: Long,
    private val readControllerUseCase: ReadControllerUseCase
) : ControllerStateChanger {
    private val uiScope = CoroutineScope(Dispatchers.Main)
    
    override fun inflate(container: ViewGroup) {
        container.inflate(R.layout.state_changer_read).also { onViewCreated(it) }
    }
    
    fun onViewCreated(view: View) {
        view.read.setMode(ActionProcessButton.Mode.ENDLESS)
        view.read.setOnClickListener {
            uiScope.launch {
                view.read.loading()
                readControllerUseCase.runCatching { execute(id) }
                    .onSuccess { view.read.idle() }
                    .onFailure { view.read.error() }
            }
        }
    }
    
    override fun onDestroy() {
    }
}

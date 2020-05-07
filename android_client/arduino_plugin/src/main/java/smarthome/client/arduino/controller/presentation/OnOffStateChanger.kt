package smarthome.client.arduino.controller.presentation

import android.view.View
import android.view.ViewGroup
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.state_changer_onoff.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import smarthome.client.arduino_plugin.R
import smarthome.client.domain.api.conrollers.usecases.ObserveControllerUseCase
import smarthome.client.domain.api.conrollers.usecases.ReadControllerUseCase
import smarthome.client.domain.api.conrollers.usecases.WriteStateToControllerUseCase
import smarthome.client.presentation.controllers.controllerdetail.statechanger.ControllerStateChanger
import smarthome.client.presentation.controllers.controllerdetail.statechanger.extensions.error
import smarthome.client.presentation.controllers.controllerdetail.statechanger.extensions.idle
import smarthome.client.presentation.controllers.controllerdetail.statechanger.extensions.loading
import smarthome.client.presentation.util.inflate
import smarthome.client.util.data
import smarthome.client.util.runInScope

private const val on = "on"
private const val off = "off"

class OnOffStateChanger(
        private val id: Long,
        private val writeStateToControllerUseCase: WriteStateToControllerUseCase,
        private val readControllerUseCase: ReadControllerUseCase,
        private val observeControllerUseCase: ObserveControllerUseCase
) : ControllerStateChanger {
    private var currentState: String? = null
    private val uiScope = CoroutineScope(Dispatchers.Main)
    private val dispose = CompositeDisposable()
    private lateinit var view: View

    override fun inflate(container: ViewGroup) {
        container.inflate(R.layout.state_changer_onoff).also {
            view = it
            onViewCreated(it)
        }
    }

    private fun onViewCreated(rootView: View) {
        observeController()

        rootView.change_button.setOnClickListener {
            uiScope.launch {
                when (currentState) {
                    null -> readState()
                    else -> changeStateToOpposite()
                }
            }
        }
    }

    private suspend fun readState() {
        view.change_button.loading()

        readControllerUseCase.runCatching { execute(id) }
                .onFailure { view.change_button.error() }
                .onSuccess { view.change_button.idle() }
    }

    private suspend fun changeStateToOpposite() {
        val wantedState = getOppositeState()

        view.change_button.loading()

        writeStateToControllerUseCase.runCatching { execute(id, wantedState) }
                .onFailure { view.change_button.error() }
                .onSuccess { view.change_button.idle() }
    }

    private fun observeController() {
        observeControllerUseCase.runInScope(uiScope) {
            dispose.add(execute(id)
                    .subscribe {
                        val newState = it.data?.state ?: return@subscribe
                        currentState = newState
                        changeIdleText()
                        changeLoadingText()
                    }
            )
        }
    }

    override fun onDestroy() {
        dispose.dispose()
    }

    private fun getOppositeState() =
            when (currentState) {
                on -> off
                else -> on
            }

    private fun changeLoadingText() {
        when (currentState) {
            on -> view.change_button.loadingText = "Switching off"
            off -> view.change_button.loadingText = "Switching on"
            else -> view.change_button.loadingText = "Reading"
        }
    }

    private fun changeIdleText() {
        when (currentState) {
            on -> view.change_button.normalText = "Switch off"
            off -> view.change_button.normalText = "Switch on"
            else -> view.change_button.normalText = "Read"
        }
    }
}
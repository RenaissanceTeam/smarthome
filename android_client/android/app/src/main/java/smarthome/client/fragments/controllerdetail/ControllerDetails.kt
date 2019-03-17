package smarthome.client.fragments.controllerdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import smarthome.client.CONTROLLER_GUID
import smarthome.client.R
import smarthome.client.fragments.controllerdetail.statechanger.ControllerStateChanger
import smarthome.client.fragments.controllerdetail.statechanger.OnOffStateChanger
import smarthome.client.fragments.controllerdetail.statechanger.ReadStateChanger
import smarthome.client.fragments.controllerdetail.statechanger.StateChangerType
import smarthome.client.ui.DialogParameters
import smarthome.client.ui.EditTextDialog
import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice
import smarthome.library.common.constants.Constants.STATE_PENDING_READ
import smarthome.library.common.constants.Constants.STATE_PENDING_WRITE

class ControllerDetails : Fragment() {

    companion object {
        val FRAGMENT_TAG = "ControllerDetailsFragment"
    }

    private val viewModel
            by lazy { ViewModelProviders.of(this).get(ControllerDetailViewModel::class.java) }

    private var device: TextView? = null
    private var name: TextView? = null
    private var type: TextView? = null
    private var serveState: TextView? = null
    private var state: TextView? = null
    private var progressBar: ProgressBar? = null
    private var stateChangerContainer: FrameLayout? = null
    private var stateChanger: ControllerStateChanger? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.refresh.observe(this, Observer { progressBar?.visibility = if (it) View.VISIBLE else View.GONE })
        viewModel.controller.observe(this, Observer {
            bindController(it)
            it.state?.let { state -> stateChanger?.invalidateNewState(state) }
        })
        viewModel.device.observe(this, Observer { bindDevice(it) })
        viewModel.stateChangerType.observe(this, Observer { invalidateStateChanger(it) })
    }

    private fun bindController(controller: BaseController) {
        setControllerName(controller)
        type?.text = controller.type.toString()
        serveState?.text = controller.serveState
        state?.text = controller.state
    }

    private fun setControllerName(controller: BaseController) {
        if (controller.name.isNullOrEmpty()) {
            name?.setTextColor(resources.getColor(android.R.color.darker_gray))
            name?.text = getString(R.string.empty_name)
        } else {
            name?.setTextColor(resources.getColor(android.R.color.black))
            name?.text = controller.name
        }
    }

    private fun bindDevice(iotDevice: IotDevice) {
        device?.text = iotDevice.name
    }

    private fun invalidateStateChanger(changerType: StateChangerType) {
        val container = stateChangerContainer ?: return

        val changer = when (changerType) {
            StateChangerType.ONOFF -> OnOffStateChanger(container) { viewModel.newStateRequest(it, STATE_PENDING_WRITE) }
            StateChangerType.ONLY_READ -> ReadStateChanger(container) { viewModel.newStateRequest(null, STATE_PENDING_READ) }
        }
        stateChanger = changer
        viewModel.controller.value?.state?.let { changer.invalidateNewState(it) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_controller_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        device = view.findViewById(R.id.device)
        name = view.findViewById(R.id.controller_name)
        type = view.findViewById(R.id.type)
        serveState = view.findViewById(R.id.serve_state)
        state = view.findViewById(R.id.state)
        progressBar = view.findViewById(R.id.progress_bar)
        stateChangerContainer = view.findViewById(R.id.state_changer)

        name?.setOnClickListener {
            EditTextDialog.create(view.context,
                    DialogParameters("controller name", viewModel.controller.value?.name
                            ?: "") {
                        viewModel.controllerNameChanged(it)
                    }
            ).show()
        }

        var controllerGuid: Long? = arguments?.getLong(CONTROLLER_GUID)
        if (arguments?.containsKey(CONTROLLER_GUID) != true) {
            controllerGuid = null
        }
        viewModel.setControllerGuid(controllerGuid)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        device = null
        name = null
        type = null
        serveState = null
        state = null
        progressBar = null
        stateChangerContainer = null
    }
}
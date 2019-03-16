package smarthome.client.fragments.controllerdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
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
import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice

class ControllerDetails : Fragment() {

    companion object {
        val FRAGMENT_TAG = "ControllerDetailsFragment"
    }

    private val viewModel
            by lazy { ViewModelProviders.of(this).get(ControllerDetailViewModel::class.java) }

    private var device: TextView? = null
    private var name: EditText? = null
    private var type: TextView? = null
    private var serveState: TextView? = null
    private var state: TextView? = null
    private var progressBar: ProgressBar? = null
    private var stateChangerContainer: FrameLayout? = null
    private var stateChanger: ControllerStateChanger? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.refresh.observe(this, Observer { progressBar?.visibility = if (it) View.VISIBLE else View.GONE })
        viewModel.controller.observe(this, Observer { bindController(it); viewModel.controllerSet() })
        viewModel.device.observe(this, Observer { bindDevice(it) })
        viewModel.stateChangerType.observe(this, Observer { invalidateStateChanger(it) })
    }

    private fun bindController(controller: BaseController) {
        name?.setText("${controller.hashCode()}") // todo name for controller
        type?.text = controller.type.toString()
        serveState?.text = if (controller.isPending) "Pending" else "Up to date" // todo make serveState accessible
        state?.text = controller.state
    }

    private fun bindDevice(iotDevice: IotDevice) {
        device?.text = iotDevice.name
    }

    private fun invalidateStateChanger(changerType: StateChangerType) {
        val container = stateChangerContainer ?: return

        val changer = when (changerType) {
            StateChangerType.ONOFF -> OnOffStateChanger(container) { viewModel.newStateRequest(it) }
            StateChangerType.ONLY_READ -> ReadStateChanger(container) { viewModel.newStateRequest(null) }
        }

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
package smarthome.client.presentation.screens.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_dashboard.*
import smarthome.client.R
import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice

class DashboardFragment : Fragment() {
    private val viewModel: DashboardViewModel by viewModels()
    private var adapterForDevices: DevicesAdapter? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.devices.observe(this) {
            adapterForDevices?.notifyDataSetChanged()
        }
        viewModel.allHomeUpdateState.observe(this) {
            refresh_layout.isRefreshing = it
        }
        viewModel.toastMessage.observe(this) {
            it ?: return@observe
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.toastShowed()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        devices.layoutManager = LinearLayoutManager(view.context)

        refresh_layout.setOnRefreshListener { viewModel.requestSmartHomeState() }
        adapterForDevices = DevicesAdapter(layoutInflater, viewModel,
                ::onDeviceClick, ::onControllerClick)
        devices?.adapter = adapterForDevices
        devices?.addItemDecoration(DividerItemDecoration(context, VERTICAL))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapterForDevices = null
    }

    private fun onDeviceClick(device: IotDevice?) {
        device ?: return

        val action = DashboardFragmentDirections.actionDashboardFragmentToDeviceDetails(device.guid)
        findNavController().navigate(action)
    }

    private fun onControllerClick(controller: BaseController) {
        val action = DashboardFragmentDirections
                .actionDashboardFragmentToControllerDetails(controller.guid)
        findNavController().navigate(action)
    }
}
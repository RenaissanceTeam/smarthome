package smarthome.client.screens.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.fragment_dashboard.*
import smarthome.client.BuildConfig
import smarthome.client.R
import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice


class DashboardFragment : Fragment() {

    private val TAG = DashboardFragment::class.java.simpleName
    private val viewModel: DashboardViewModel by viewModels()

    private var adapterForDevices: DevicesAdapter? = null


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (BuildConfig.DEBUG) Log.d(TAG, "onActivity created, create viewModel")
        viewModel.devices.observe(this) {
            if (BuildConfig.DEBUG) Log.d(TAG, "devicesView've changed, now its ${viewModel.devices.value}")
            adapterForDevices?.notifyDataSetChanged()
        }
        viewModel.allHomeUpdateState.observe(this) {
            if (BuildConfig.DEBUG) Log.d(TAG, "allHomeUpdateState's changed")
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

        if (BuildConfig.DEBUG) Log.d(TAG, "onViewCreated")

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
        Log.d(TAG, "clicked on $device")
        device ?: return

        val action = DashboardFragmentDirections.actionDashboardFragmentToDeviceDetails(device.guid)
        findNavController().navigate(action)
    }

    private fun onControllerClick(controller: BaseController) {
        Log.d(TAG, "clicked on $controller")

        val action = DashboardFragmentDirections
                .actionDashboardFragmentToControllerDetails(controller.guid)
        findNavController().navigate(action)
    }
}
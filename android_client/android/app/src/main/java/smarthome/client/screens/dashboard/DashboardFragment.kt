package smarthome.client.screens.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import smarthome.client.BuildConfig
import smarthome.client.R
import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice


class DashboardFragment : Fragment() {

    private val TAG = DashboardFragment::class.java.simpleName
    private val viewModel
            by lazy { ViewModelProviders.of(this).get(DashboardViewModel::class.java) }

    private var refreshLayout: SwipeRefreshLayout? = null
    private var devicesView: RecyclerView? = null
    private var adapterForDevices: DevicesAdapter? = null


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (BuildConfig.DEBUG) Log.d(TAG, "onActivity created, create viewModel")
        viewModel.devices.observe(this, Observer {
            if (BuildConfig.DEBUG) Log.d(TAG, "devicesView've changed, now its ${viewModel.devices.value}")
            adapterForDevices?.notifyDataSetChanged()
        })
        viewModel.allHomeUpdateState.observe(this, Observer {
            if (BuildConfig.DEBUG) Log.d(TAG, "allHomeUpdateState's changed")
            refreshLayout?.isRefreshing = it
        })
        viewModel.toastMessage.observe(this, Observer {
            it ?: return@Observer
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.toastShowed()
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (BuildConfig.DEBUG) Log.d(TAG, "onViewCreated")
        refreshLayout = view.findViewById(R.id.refresh_layout)
        devicesView = view.findViewById(R.id.devices)
        devicesView?.layoutManager = LinearLayoutManager(view.context)

        refreshLayout?.setOnRefreshListener { viewModel.requestSmartHomeState() }
        adapterForDevices = DevicesAdapter(layoutInflater, viewModel,
                ::onDeviceClick, ::onControllerClick)
        devicesView?.adapter = adapterForDevices
        devicesView?.addItemDecoration(DividerItemDecoration(context, VERTICAL))
    }

    override fun onDestroyView() {
        super.onDestroyView()

        refreshLayout = null
        devicesView = null
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
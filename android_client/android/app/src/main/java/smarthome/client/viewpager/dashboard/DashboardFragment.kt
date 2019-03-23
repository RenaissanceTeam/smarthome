package smarthome.client.viewpager.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import smarthome.client.*
import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL


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
            viewModel.receivedNewSmartHomeState()
        })
        viewModel.allHomeUpdateState.observe(this, Observer {
            if (BuildConfig.DEBUG) Log.d(TAG, "allHomeUpdateState's changed")
            refreshLayout?.isRefreshing = it
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

        activity?.startActivity(Intent(context, DetailsActivity::class.java)
                .putExtra(DEVICE_GUID, device.guid))
    }

    private fun onControllerClick(controller: BaseController) {
        Log.d(TAG, "clicked on $controller")

        activity?.startActivity(Intent(context, DetailsActivity::class.java)
                .putExtra(CONTROLLER_GUID, controller.guid))
    }
}
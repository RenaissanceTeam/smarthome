package smarthome.client.dashboard

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
import smarthome.client.BuildConfig
import smarthome.client.R
import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice

class DashboardFragment : Fragment() {

    private val TAG = DashboardFragment::class.java.simpleName
    private val viewModel
            by lazy { ViewModelProviders.of(this).get(DashboardViewModel::class.java) }

    private var refreshLayout: SwipeRefreshLayout? = null
    private var controllersView: RecyclerView? = null
    private var adapterForControllersList: DevicesAdapter? = null


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        
        if (BuildConfig.DEBUG) Log.d(TAG, "onActivity created, create viewModel")
        viewModel.devices.observe(this, Observer {
            if (BuildConfig.DEBUG) Log.d(TAG, "controllersView've changed, now its ${viewModel.devices.value}")
            adapterForControllersList?.notifyDataSetChanged()
            viewModel.receivedNewSmartHomeState()
        })
        viewModel.allHomeUpdateState.observe(this, Observer {
            if (BuildConfig.DEBUG) Log.d(TAG, "allHomeUpdateState's changed")
            refreshLayout?.isRefreshing = it 
        })

        adapterForControllersList = DevicesAdapter(layoutInflater, viewModel,
                ::onDeviceClick, ::onControllerClick)
        controllersView?.adapter = adapterForControllersList
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (BuildConfig.DEBUG) Log.d(TAG, "onViewCreated")
        refreshLayout = view.findViewById(R.id.refresh_controllers)
        controllersView = view.findViewById(R.id.controllers)
        controllersView?.layoutManager = LinearLayoutManager(view.context)

        refreshLayout?.setOnRefreshListener { viewModel.requestSmartHomeState() }
    }

    private fun onDeviceClick(device: IotDevice?) {
        Log.d(TAG, "clicked on $device")
    }

    private fun onControllerClick(controller: BaseController) {
        Log.d(TAG, "clicked on $controller")

    }
}
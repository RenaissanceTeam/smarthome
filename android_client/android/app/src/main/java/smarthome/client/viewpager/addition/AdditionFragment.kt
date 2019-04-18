package smarthome.client.viewpager.addition

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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import smarthome.client.*
import smarthome.client.fragments.deviceaddition.DISCOVER_REQUEST_CODE
import smarthome.client.util.CloudStorages
import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice
import smarthome.library.common.message.DiscoverAllDevicesRequest

class AdditionFragment : Fragment(), ViewNotifier {

    private val TAG = javaClass.name

    private var refreshLayout: SwipeRefreshLayout? = null
    private var fab: FloatingActionButton? = null
    private var devicesRecycler: RecyclerView? = null
    private var adapterForDevices: DeviceAdapter? = null

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    private val viewModel
            by lazy { ViewModelProviders.of(this).get(AdditionViewModel::class.java) }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (BuildConfig.DEBUG) Log.d(TAG, "onActivity created, create viewModel")
        viewModel.devices.observe(this, Observer {
            if (BuildConfig.DEBUG) Log.d(TAG, "devicesRecycler've changed, now its ${viewModel.devices.value}")
            adapterForDevices?.notifyDataSetChanged()
        })
        viewModel.allHomeUpdateState.observe(this, Observer {
            if (BuildConfig.DEBUG) Log.d(TAG, "allHomeUpdateState's changed")
            refreshLayout?.isRefreshing = it
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_device_addition, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (BuildConfig.DEBUG) Log.d(TAG, "onViewCreated")
        refreshLayout = view.findViewById(R.id.add_device_refresh_layout)
        fab = view.findViewById(R.id.add_device_fab)
        fab?.setOnClickListener {
            startActivityForResult(Intent(context, AddDeviceActivity::class.java), DISCOVER_REQUEST_CODE)
        }

        devicesRecycler = view.findViewById(R.id.add_device_recycler)
        devicesRecycler?.layoutManager = LinearLayoutManager(view.context)

        refreshLayout?.setOnRefreshListener { viewModel.requestSmartHomeState() }
        resetAdapter()
        devicesRecycler?.setHasFixedSize(true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == DISCOVER_REQUEST_CODE) {
            resetAdapter()
        }
    }

    private fun onDeviceAccept(device: IotDevice?) {
        Log.d(TAG, "clicked on $device")
        device ?: return

        device.setAccepted()
        updateDevice(device)
    }

    private fun onDeviceReject(device: IotDevice?) {
        Log.d(TAG, "clicked on $device")
        device ?: return

        device.setRejected()
        updateDevice(device)
    }

    private fun onDeviceDetailsClick(device: IotDevice?) {
        Log.d(TAG, "clicked on $device")
        device ?: return

        activity?.startActivity(Intent(context, DetailsActivity::class.java)
                .putExtra(DEVICE_GUID, device.guid)
                .putExtra(USE_PENDING, true))
    }

    private fun onControllerDetailsClick(controller: BaseController?) {
        controller ?: return

        activity?.startActivity(Intent(context, DetailsActivity::class.java)
                .putExtra(CONTROLLER_GUID, controller.guid)
                .putExtra(USE_PENDING, true))
    }

    private fun onControllerChanged(controller: BaseController?) {
        uiScope.launch {
            controller?.let { viewModel.onControllerChanged(it) }
        }
    }

    private fun updateDevice(device: IotDevice) {
        uiScope.launch {
            Model.changePendingDevice(device)
        }
    }

    private fun onAddDeviceClicked() {
        uiScope.launch {
            CloudStorages.getMessageQueue()
                    .postMessage(DiscoverAllDevicesRequest("1")) //TODO implement clientID generation
        }
    }

    override fun onItemRemoved(pos: Int) {
        resetAdapter()
    }

    private fun resetAdapter() {
        adapterForDevices = createDeviceAdapter()
        devicesRecycler?.adapter = adapterForDevices
        adapterForDevices?.viewNotifier = this
    }

    private fun createDeviceAdapter(): DeviceAdapter {
        return DeviceAdapter(viewModel,
                ::onDeviceDetailsClick,
                ::onControllerDetailsClick,
                ::onDeviceAccept,
                ::onDeviceReject,
                ::onControllerChanged)
    }
}
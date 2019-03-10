package smarthome.client.fragments.devicedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import smarthome.client.R
import smarthome.client.DEVICE_GUID
import smarthome.library.common.IotDevice

class DeviceDetails : Fragment() {

    private val viewModel by lazy { ViewModelProviders.of(this).get(DeviceDetailViewModel::class.java) }

    private var deviceName: EditText? = null
    private var deviceDescription: EditText? = null
    private var deviceImage: ImageView? = null
    private var progressBar: ProgressBar? = null
    private var controllers: RecyclerView? = null

    companion object {
        val FRAGMENT_TAG = "DeviceDetailsFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.device_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.refresh.observe(this, Observer { progressBar?.visibility = if (it) VISIBLE else GONE })
        viewModel.device.observe(this, Observer { bindDevice(it); viewModel.deviceSet() })
    }

    private fun bindDevice(device: IotDevice) {
        deviceName?.setText(device.name)
        deviceDescription?.setText(device.description)

        // todo bind controllers
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        deviceName = view.findViewById(R.id.device_name)
        deviceDescription = view.findViewById(R.id.device_description)
        deviceImage = view.findViewById(R.id.device_image)
        progressBar = view.findViewById(R.id.progress_bar)
        controllers = view.findViewById(R.id.controllers)

        var deviceGuid: Long? = arguments?.getLong(DEVICE_GUID)
        if (arguments?.containsKey(DEVICE_GUID) != true) {
            deviceGuid = null
        }
        viewModel.setDeviceGuid(deviceGuid)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        deviceName = null
        deviceDescription = null
        deviceImage = null
        progressBar = null
        controllers = null
    }
}
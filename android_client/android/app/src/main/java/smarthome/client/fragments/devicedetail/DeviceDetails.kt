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
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import smarthome.client.CONTROLLER_GUID
import smarthome.client.R
import smarthome.client.DEVICE_GUID
import smarthome.client.DetailsActivity
import smarthome.client.ui.DialogParameters
import smarthome.client.ui.EditTextDialog
import smarthome.library.common.IotDevice

class DeviceDetails : Fragment() {

    private val viewModel by lazy { ViewModelProviders.of(this).get(DeviceDetailViewModel::class.java) }

    private var deviceName: TextView? = null
    private var deviceDescription: TextView? = null
    private var deviceImage: ImageView? = null
    private var progressBar: ProgressBar? = null
    private var controllers: RecyclerView? = null

    companion object {
        val FRAGMENT_TAG = "DeviceDetailsFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_device_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.refresh.observe(this, Observer { progressBar?.visibility = if (it) VISIBLE else GONE })
        viewModel.device.observe(this, Observer { bindDevice(it) })
        viewModel.controllerDetails.observe(this, Observer { it?.let { openControllerDetails(it) } })
    }

    private fun bindDevice(device: IotDevice) {
        deviceName?.text = device.name
        setDescription(device)
        controllers?.adapter?.notifyDataSetChanged()
    }

    private fun setDescription(device: IotDevice) {
        val description = device.description
        if (description.isNullOrEmpty()) {
            deviceDescription?.setTextColor(resources.getColor(android.R.color.darker_gray))
            deviceDescription?.text = getString(R.string.empty_description)
        } else {
            deviceDescription?.setTextColor(resources.getColor(android.R.color.black))
            deviceDescription?.text = device.description
        }
    }

    private fun openControllerDetails(guid: Long) {
        val bundle = Bundle()
        bundle.putLong(CONTROLLER_GUID, guid)
        (activity as? DetailsActivity)?.replaceFragment(bundle)
        viewModel.controllerDetailsShowed()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViews(view)
        passGuidToViewModel()

        deviceName?.setOnClickListener {
            EditTextDialog.create(view.context,
                    DialogParameters("device name", viewModel.device.value?.name ?: "") {
                        viewModel.deviceNameChanged(it)
                    }
            ).show()
        }

        deviceDescription?.setOnClickListener {
            EditTextDialog.create(view.context,
                    DialogParameters("device description", viewModel.device.value?.description ?: "") {
                        viewModel.deviceDescriptionChanged(it)
                    }
            ).show()
        }
    }

    private fun setupViews(view: View) {
        deviceName = view.findViewById(R.id.device_name)
        deviceDescription = view.findViewById(R.id.device_description)
        deviceImage = view.findViewById(R.id.device_image)
        progressBar = view.findViewById(R.id.progress_bar)
        controllers = view.findViewById(R.id.devices)
        controllers?.layoutManager = LinearLayoutManager(view.context)
        controllers?.adapter = ControllersAdapter(viewModel)
    }

    private fun passGuidToViewModel() {
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
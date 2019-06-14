package smarthome.client.fragments.devicedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.device_card.*
import kotlinx.android.synthetic.main.fragment_device_details.*
import smarthome.client.*
import smarthome.client.ui.DialogParameters
import smarthome.client.ui.EditTextDialog
import smarthome.library.common.IotDevice

class DeviceDetails : Fragment() {
    private val viewModel by lazy { ViewModelProviders.of(this).get(DeviceDetailViewModel::class.java) }
    private val args: DeviceDetailsArgs by navArgs()

    companion object {
        val FRAGMENT_TAG = "DeviceDetailsFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_device_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.refresh.observe(this, Observer { progress_bar.visibility = if (it) VISIBLE else GONE })
        viewModel.device.observe(this, Observer { bindDevice(it) })
        viewModel.controllerDetails.observe(this, Observer { it?.let { openControllerDetails(it) } })
    }

    private fun bindDevice(device: IotDevice) {
        device_name.text = device.name
        setDescription(device)
        device_controllers_recycler.adapter?.notifyDataSetChanged()
    }

    private fun setDescription(device: IotDevice) {
        val description = device.description
        if (description.isNullOrEmpty()) {
            device_description.setTextColor(resources.getColor(android.R.color.darker_gray))
            device_description.text = getString(R.string.empty_description)
        } else {
            device_description.setTextColor(resources.getColor(android.R.color.black))
            device_description.text = device.description
        }
    }

    private fun openControllerDetails(guid: Long) {
        val action = DeviceDetailsDirections.actionDeviceDetailsToControllerDetails(guid)
        findNavController().navigate(action)

        viewModel.controllerDetailsShowed()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        device_controllers_recycler.layoutManager = LinearLayoutManager(view.context)
        device_controllers_recycler.adapter = ControllersAdapter(viewModel)
        device_controllers_recycler.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        if (args.usePending) viewModel.usePending()
        viewModel.setDeviceGuid(args.deviceGuid)

        device_name.setOnClickListener {
            EditTextDialog.create(view.context,
                    DialogParameters("device name", currentValue = viewModel.device.value?.name ?: "") {
                        viewModel.deviceNameChanged(it)
                    }
            ).show()
        }

        device_description.setOnClickListener {
            EditTextDialog.create(view.context,
                    DialogParameters("device description", currentValue = viewModel.device.value?.description ?: "") {
                        viewModel.deviceDescriptionChanged(it)
                    }
            ).show()
        }
    }
}
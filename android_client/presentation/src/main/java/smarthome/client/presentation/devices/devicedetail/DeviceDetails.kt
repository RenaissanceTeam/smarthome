package smarthome.client.presentation.devices.devicedetail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_device_details.*
import smarthome.client.entity.Device
import smarthome.client.presentation.R
import smarthome.client.presentation.core.BaseFragment
import smarthome.client.presentation.devices.devicedetail.epoxy.DeviceDetailsController
import smarthome.client.presentation.ui.DialogParameters
import smarthome.client.presentation.ui.EditTextDialog
import smarthome.client.presentation.util.extensions.setTextOrEmptyPlaceholder
import smarthome.client.presentation.util.extensions.showToast
import smarthome.client.util.visible


class DeviceDetails : BaseFragment() {
    private val viewModel: DeviceDetailViewModel by viewModels()
    private val args: DeviceDetailsArgs by navArgs()
    private val itemsController = DeviceDetailsController()

    override fun getLayout() = R.layout.fragment_device_details

    private fun bindDevice(device: Device) {
        device_name.setTextOrEmptyPlaceholder(device.name, "Empty name")
        device_description.setTextOrEmptyPlaceholder(device.description, "Empty description")
        type.text = device.type
    }

    private fun openControllerDetails(controllerId: Long) {
        val action = DeviceDetailsDirections.actionDeviceDetailsToControllerDetails(controllerId)
        findNavController().navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        controllers.layoutManager = LinearLayoutManager(view.context)
        controllers.adapter = itemsController.adapter

        controllers.addItemDecoration(
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        device_name.setOnClickListener {
            EditTextDialog.create(view.context,
                    DialogParameters("device name",
                            currentValue = viewModel.currentDeviceName()) {
                        viewModel.deviceNameChanged(it)
                    }
            ).show()
        }

        device_description.setOnClickListener {
            EditTextDialog.create(view.context,
                    DialogParameters("device description",
                            currentValue = viewModel.currentDeviceDescription()) {
                        viewModel.deviceDescriptionChanged(it)
                    }
            ).show()
        }

        lifecycle.addObserver(viewModel)

        viewModel.refresh.observe(viewLifecycleOwner) { progress_bar.visible = it }
        viewModel.device.observe(viewLifecycleOwner, ::bindDevice)
        viewModel.errors.onToast(viewLifecycleOwner) { context?.showToast(it) }
        viewModel.controllersLiveData.observe(viewLifecycleOwner) {
            itemsController.setData(it, viewModel)
        }

        viewModel.openController.onNavigate(this, ::openControllerDetails)
        viewModel.setDeviceId(args.deviceGuid)
    }
}
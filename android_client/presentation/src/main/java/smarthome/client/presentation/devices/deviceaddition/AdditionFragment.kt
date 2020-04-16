package smarthome.client.presentation.devices.deviceaddition

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_device_addition.*
import smarthome.client.presentation.R
import smarthome.client.presentation.core.BaseFragment
import smarthome.client.presentation.devices.deviceaddition.epoxy.PendingDeviceController
import smarthome.client.presentation.util.extensions.showNotImplementedToast
import smarthome.client.presentation.util.extensions.showToast

class AdditionFragment : BaseFragment() {
    private val controller = PendingDeviceController()
    private val viewModel: AdditionViewModel by viewModels()

    override fun getLayout() = R.layout.fragment_device_addition

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycle.addObserver(viewModel)
        viewModel.deviceStates.observe(viewLifecycleOwner) {
            controller.setData(it, viewModel)
        }
        viewModel.refresh.observe(viewLifecycleOwner) {
            refresh_layout.isRefreshing = it
        }
        viewModel.openDeviceDetails.onNavigate(this) {
            view.findNavController()
                    .navigate(AdditionFragmentDirections.actionAdditionFragmentToDeviceDetails(it))
        }
        viewModel.openControllerDetails.onNavigate(this) {
            view.findNavController()
                    .navigate(AdditionFragmentDirections.actionAdditionFragmentToControllerDetails(it))
        }

        add_device_fab.setOnClickListener {
            viewModel.onAddDeviceClicked()
        }

        refresh_layout.setOnRefreshListener(viewModel::onRefresh)

        add_device_recycler.layoutManager = LinearLayoutManager(view.context)
        add_device_recycler.adapter = controller.adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == DISCOVER_REQUEST_CODE) {
            context?.showNotImplementedToast()
        }
    }
}
package smarthome.client.presentation.location

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.home_geofence_fragment.*
import org.koin.android.ext.android.inject
import smarthome.client.presentation.R
import smarthome.client.presentation.core.BaseFragment
import smarthome.client.presentation.main.toolbar.ToolbarController
import smarthome.client.presentation.util.extensions.setOnChangeListener

class SetupHomeGeofenceFragment : BaseFragment() {
    private val viewModel: SetupHomeGeofenceViewModel by viewModels()
    private val toolbarController: ToolbarController by inject()

    override fun getLayout() = R.layout.home_geofence_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycle.addObserver(viewModel)
        toolbarController.setTitle("Setup the home zone")
        toolbarController.setMenu(R.menu.save) {
            if (it != R.id.save) return@setMenu
            viewModel.onSave()
        }

        geofence_radius.setOnChangeListener(viewModel::onChangeRadius)
        viewModel.homeGeofence.observe(viewLifecycleOwner) {
            map.setGeofence(it)
            if (!geofence_radius.hasFocus()) geofence_radius.progress = it.radius
        }

        viewModel.close.onNavigate(viewLifecycleOwner) {
            findNavController().popBackStack()
        }

        map.onCreate(savedInstanceState)
        map.getMapAsync { readyMap ->
            with(readyMap) {
                setOnMapClickListener {
                    viewModel.setHomePosition(it.latitude, it.longitude)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        map.onResume()
    }

    override fun onPause() {
        super.onPause()

        map.onPause()
    }
}

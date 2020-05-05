package smarthome.client.presentation.location

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.home_geofence_fragment.*
import org.koin.android.ext.android.inject
import smarthome.client.entity.location.HomeGeofence
import smarthome.client.presentation.R
import smarthome.client.presentation.core.BaseFragment
import smarthome.client.presentation.main.toolbar.ToolbarController
import smarthome.client.presentation.util.extensions.setOnChangeListener
import smarthome.client.util.fold

class SetupHomeGeofenceFragment : BaseFragment() {
    private val viewModel: SetupHomeGeofenceViewModel by viewModels()
    private var googleApiClient: GoogleApiClient? = null
    private var googleMap: GoogleMap? = null
    private var homeMarker: Marker? = null
    private lateinit var homeMarkerImage: Bitmap
    private lateinit var homeMarkerImageDescriptor: BitmapDescriptor
    private var homeCircle: Circle? = null
    private val toolbarController: ToolbarController by inject()
    private var initialZoomSet = false

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
            updateMap(it)
            if (!geofence_radius.hasFocus()) geofence_radius.progress = it.radius
        }

        viewModel.close.onNavigate(viewLifecycleOwner) {
            findNavController().popBackStack()
        }

        map.onCreate(savedInstanceState)
        map.getMapAsync { readyMap ->
            with(readyMap) {
                initializeHomeMarkerImage()

                googleMap = this
                isMyLocationEnabled = true

                setOnMapClickListener {
                    viewModel.setHomePosition(it.latitude, it.longitude)
                }
            }
        }
    }

    private fun updateMap(it: HomeGeofence) {
        val position = LatLng(it.lat, it.lon)

        updateHomeMarker(position)
        updateHomeCircle(position, it)

        googleMap?.animateCamera(
                initialZoomSet.fold(
                        ifTrue = { CameraUpdateFactory.newLatLng(position) },
                        ifFalse = {
                            initialZoomSet = true
                            CameraUpdateFactory.newLatLngZoom(position, 17f)
                        }
                )
        )
    }

    private fun initializeHomeMarkerImage() {
        homeMarkerImage = resources.getDrawable(R.drawable.ic_home, null).let {
            it.toBitmap(it.intrinsicWidth, it.intrinsicHeight, null)
        }

        homeMarkerImageDescriptor = BitmapDescriptorFactory.fromBitmap(homeMarkerImage)
    }

    private fun updateHomeMarker(position: LatLng) {
        homeMarker?.remove()
        homeMarker = googleMap?.addMarker(
                MarkerOptions()
                        .position(position)
                        .anchor(0.5f, 0.5f)
                        .icon(homeMarkerImageDescriptor)
        )
    }

    private fun updateHomeCircle(position: LatLng, fence: HomeGeofence) {
        homeCircle?.remove()
        homeCircle = googleMap?.addCircle(
                CircleOptions()
                        .center(position)
                        .radius(fence.radius.toDouble())
                        .fillColor(resources.getColor(R.color.homeZone, null))
                        .strokeColor(resources.getColor(R.color.homeZoneBorder, null))
        )
    }

    override fun onStart() {
        super.onStart()

        googleApiClient?.connect()
    }

    override fun onStop() {
        super.onStop()

        googleApiClient?.disconnect()
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

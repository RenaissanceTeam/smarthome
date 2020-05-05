package smarthome.client.presentation.location

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.observe
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.home_geofence_fragment.*
import org.koin.core.inject
import smarthome.client.entity.location.HomeGeofence
import smarthome.client.presentation.R
import smarthome.client.presentation.core.BaseFragment
import smarthome.client.presentation.main.toolbar.ToolbarController
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.util.extensions.setOnChangeListener
import smarthome.client.presentation.util.extensions.updateWith

class SetupHomeGeofenceFragment : BaseFragment() {
    private val viewModel: SetupHomeGeofenceViewModel by viewModels()
    private var googleApiClient: GoogleApiClient? = null
    private var googleMap: GoogleMap? = null
    private var homeMarker: Marker? = null
    private lateinit var homeMarkerImage: Bitmap
    private lateinit var homeMarkerImageDescriptor: BitmapDescriptor
    private var homeCircle: Circle? = null

    override fun getLayout() = R.layout.home_geofence_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycle.addObserver(viewModel)

        geofencce_radius.setOnChangeListener(viewModel::onChangeRadius)
        viewModel.homeGeofence.observe(viewLifecycleOwner) {
            val position = LatLng(it.lat, it.lon)

            updateHomeMarker(position)
            updateHomeCircle(position, it)

            googleMap?.animateCamera(CameraUpdateFactory.newLatLng(position))
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

class SetupHomeGeofenceViewModel : KoinViewModel() {
    private val toolbarController: ToolbarController by inject()
    val homeGeofence = MutableLiveData<HomeGeofence>()

    override fun onResume() {
        toolbarController.setTitle("Setup the home zone")
    }

    fun setHomePosition(latitude: Double, longitude: Double) {
        homeGeofence.updateWith {
            (it ?: HomeGeofence()).copy(lat = latitude, lon = longitude)
        }
    }

    fun onChangeRadius(newRadius: Int) {
        homeGeofence.updateWith {
            (it ?: HomeGeofence()).copy(radius = newRadius)
        }
    }
}
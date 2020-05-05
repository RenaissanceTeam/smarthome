package smarthome.client.presentation.components

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.graphics.drawable.toBitmap
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import smarthome.client.entity.location.HomeGeofence
import smarthome.client.presentation.R
import smarthome.client.util.fold


class HomeGeofenceMapView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : MapView(context, attrs, defStyleAttr) {
    private var initialZoomSet = false
    private var homeMarker: Marker? = null
    private lateinit var homeMarkerImage: Bitmap
    private lateinit var homeMarkerImageDescriptor: BitmapDescriptor
    private var homeCircle: Circle? = null
    private var googleMap: GoogleMap? = null

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        /**
         * Request all parents to relinquish the touch events
         */
        parent.requestDisallowInterceptTouchEvent(true)
        return super.dispatchTouchEvent(ev)
    }

    override fun getMapAsync(delegate: OnMapReadyCallback) {
        super.getMapAsync { readyMap ->
            delegate.onMapReady(readyMap)

            with(readyMap) {
                initializeHomeMarkerImage()

                googleMap = this
                isMyLocationEnabled = true
            }
        }
    }


    private fun initializeHomeMarkerImage() {
        homeMarkerImage = resources.getDrawable(R.drawable.ic_home, null).let {
            it.toBitmap(it.intrinsicWidth, it.intrinsicHeight, null)
        }

        homeMarkerImageDescriptor = BitmapDescriptorFactory.fromBitmap(homeMarkerImage)
    }

    fun setGeofence(geofence: HomeGeofence) {

        val position = LatLng(geofence.lat, geofence.lon)

        updateHomeMarker(position)
        updateHomeCircle(position, geofence)

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
}
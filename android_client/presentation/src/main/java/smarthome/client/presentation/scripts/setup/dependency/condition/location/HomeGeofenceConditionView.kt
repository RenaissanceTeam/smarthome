package smarthome.client.presentation.scripts.setup.dependency.condition.location

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import kotlinx.android.synthetic.main.scripts_home_geofence_condition.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import smarthome.client.presentation.R
import smarthome.client.presentation.scripts.setup.dependency.DependencyUnitView
import smarthome.client.presentation.util.inflate
import smarthome.client.presentation.util.viewScope
import smarthome.client.util.log

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class HomeGeofenceConditionView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : DependencyUnitView(context, attrs, defStyleAttr), OnMapReadyCallback, LifecycleObserver {

    var inside: Boolean = false @ModelProp set
    var onChangeInside: ((Boolean) -> Unit)? = null @CallbackProp set
    var googleMap: GoogleMap? = null

    override fun onCreateView(viewGroup: ViewGroup) {
        viewGroup.inflate(R.layout.scripts_home_geofence_condition)
    }

    @AfterPropsSet
    fun onPropsReady() {
        if (!geofence_checkbox.hasFocus()) geofence_checkbox.isChecked = inside
        geofence_checkbox.setOnCheckedChangeListener { _, isChecked -> onChangeInside?.invoke(isChecked) }

        geofence_description.text = if (inside) "On enter home zone" else "On exit home zone"
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        map.onCreate(null)
        map.getMapAsync(this)
        map.onStart()
        map.onResume()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        map.onPause()
        map.onStop()
        map.onDestroy()
    }
}
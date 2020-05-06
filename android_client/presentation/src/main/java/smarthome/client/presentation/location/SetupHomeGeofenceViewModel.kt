package smarthome.client.presentation.location

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import org.koin.core.inject
import smarthome.client.domain.api.location.usecases.GetGeofenceUseCase
import smarthome.client.domain.api.location.usecases.SetupGeofenceUseCase
import smarthome.client.entity.location.HomeGeofence
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.util.NavigationLiveData
import smarthome.client.presentation.util.extensions.updateWith
import smarthome.client.util.runInScope

class SetupHomeGeofenceViewModel : KoinViewModel() {
    val homeGeofence = MutableLiveData<HomeGeofence>()
    val close = NavigationLiveData()
    private val setupGeofenceUseCase: SetupGeofenceUseCase by inject()
    private val getGeofenceUseCase: GetGeofenceUseCase by inject()

    override fun onResume() {
        super.onResume()

        getGeofenceUseCase.runInScope(viewModelScope) {
            execute()?.let { homeGeofence.value = it }
        }
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

    fun onSave() {
        homeGeofence.value?.let {
            setupGeofenceUseCase.runInScope(viewModelScope) {
                execute(it)
                close.trigger()
            }
        }
    }
}
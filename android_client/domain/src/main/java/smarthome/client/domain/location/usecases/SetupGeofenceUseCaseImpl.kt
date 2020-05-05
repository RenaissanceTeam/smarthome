package smarthome.client.domain.location.usecases

import smarthome.client.data.api.location.LocationRepo
import smarthome.client.domain.api.location.usecases.SetupGeofenceUseCase
import smarthome.client.entity.location.HomeGeofence

class SetupGeofenceUseCaseImpl(
        private val locationRepo: LocationRepo
) : SetupGeofenceUseCase {
    override suspend fun execute(lat: Double, long: Double, radius: Int) {
        locationRepo.add(HomeGeofence(
                lat = lat,
                lon = long,
                radius = radius
        ))
    }
}
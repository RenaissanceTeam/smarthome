package smarthome.client.domain.location.usecases

import smarthome.client.data.api.location.LocationRepo
import smarthome.client.domain.api.location.usecases.GetGeofenceUseCase
import smarthome.client.entity.location.HomeGeofence

class GetGeofenceUseCaseImpl(
        private val locationRepo: LocationRepo
) : GetGeofenceUseCase {
    override suspend fun execute(): HomeGeofence? {
        return locationRepo.get()
    }
}
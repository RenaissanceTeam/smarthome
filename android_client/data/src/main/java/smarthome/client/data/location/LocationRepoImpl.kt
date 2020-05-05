package smarthome.client.data.location

import smarthome.client.data.api.location.LocationRepo
import smarthome.client.entity.location.HomeGeofence

class LocationRepoImpl(
        private val homeGeofenceRepository: HomeGeofenceRepository
) : LocationRepo {
    override suspend fun add(homeGeofence: HomeGeofence) {
        homeGeofenceRepository.save(homeGeofence)
    }
}
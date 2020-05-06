package smarthome.client.data.location

import smarthome.client.data.api.location.LocationRepo
import smarthome.client.entity.location.HomeGeofence

class LocationRepoImpl(
        private val homeGeofenceRepository: HomeGeofenceRepository
) : LocationRepo {
    override suspend fun save(homeGeofence: HomeGeofence) {
        if (homeGeofenceRepository.get() != null) {
            homeGeofenceRepository.update(homeGeofence)
        } else {
            homeGeofenceRepository.save(homeGeofence)
        }
    }

    override suspend fun get(): HomeGeofence? {
        return homeGeofenceRepository.get()
    }
}
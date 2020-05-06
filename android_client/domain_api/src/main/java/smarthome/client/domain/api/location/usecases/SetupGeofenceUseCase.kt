package smarthome.client.domain.api.location.usecases

import smarthome.client.entity.location.HomeGeofence

interface SetupGeofenceUseCase {
    suspend fun execute(geofence: HomeGeofence)
}
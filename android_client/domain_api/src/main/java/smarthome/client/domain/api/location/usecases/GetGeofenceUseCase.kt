package smarthome.client.domain.api.location.usecases

import smarthome.client.entity.location.HomeGeofence

interface GetGeofenceUseCase {
    suspend fun execute(): HomeGeofence?
}

package smarthome.client.data.api.location

import smarthome.client.entity.location.HomeGeofence

interface LocationRepo {
    suspend fun save(homeGeofence: HomeGeofence)
    suspend fun get(): HomeGeofence?
}
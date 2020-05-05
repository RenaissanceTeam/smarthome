package smarthome.client.data.api.location

import smarthome.client.entity.location.HomeGeofence

interface LocationRepo {
    suspend fun add(homeGeofence: HomeGeofence)
}
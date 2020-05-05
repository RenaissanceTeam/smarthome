package smarthome.client.domain.api.location.usecases

interface SetupGeofenceUseCase {
    suspend fun execute(lat: Long, long: Long, radius: Int)
}
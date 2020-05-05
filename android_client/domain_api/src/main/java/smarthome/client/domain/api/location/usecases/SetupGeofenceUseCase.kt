package smarthome.client.domain.api.location.usecases

interface SetupGeofenceUseCase {
    suspend fun execute(lat: Double, long: Double, radius: Int)
}
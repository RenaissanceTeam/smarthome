package smarthome.client.domain.api.devices.usecase

interface UpdateDeviceDescriptionUseCase {
    suspend fun execute(deviceId: Long, description: String)
}
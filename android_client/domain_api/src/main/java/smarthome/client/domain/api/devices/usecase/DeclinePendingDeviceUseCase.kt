package smarthome.client.domain.api.devices.usecase

interface DeclinePendingDeviceUseCase {
    suspend fun execute(id: Long)
}
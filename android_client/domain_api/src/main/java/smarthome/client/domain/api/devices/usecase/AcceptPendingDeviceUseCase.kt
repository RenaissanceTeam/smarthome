package smarthome.client.domain.api.devices.usecase

interface AcceptPendingDeviceUseCase {
    suspend fun execute(id: Long)
}


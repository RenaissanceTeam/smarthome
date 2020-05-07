package smarthome.client.domain.api.devices.usecase


interface UpdateDeviceNameUseCase {
    suspend fun execute(deviceId: Long, name: String)
}


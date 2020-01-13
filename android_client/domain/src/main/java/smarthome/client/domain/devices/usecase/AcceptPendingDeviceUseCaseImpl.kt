package smarthome.client.domain.devices.usecase

import smarthome.client.data.api.devices.DevicesRepo
import smarthome.client.domain.api.devices.usecase.AcceptPendingDeviceUseCase

class AcceptPendingDeviceUseCaseImpl(
    private val repo: DevicesRepo
) : AcceptPendingDeviceUseCase {
    override suspend fun execute(id: Long) {
        repo.acceptPending(id)
    }
}
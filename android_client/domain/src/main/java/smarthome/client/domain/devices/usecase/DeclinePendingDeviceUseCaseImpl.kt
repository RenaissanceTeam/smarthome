package smarthome.client.domain.devices.usecase

import smarthome.client.data.api.devices.DevicesRepo
import smarthome.client.domain.api.devices.usecase.DeclinePendingDeviceUseCase

class DeclinePendingDeviceUseCaseImpl(
    private val repo: DevicesRepo
) : DeclinePendingDeviceUseCase {
    override suspend fun execute(id: Long) {
        repo.declinePending(id)
    }
}
package smarthome.client.domain.api.auth.usecases

import smarthome.client.util.DataStatus

interface GetCurrentTokenUseCase {
    fun execute(): DataStatus<String>
}
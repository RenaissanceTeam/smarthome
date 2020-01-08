package smarthome.client.domain.api.homeserver.usecases

import smarthome.client.domain.api.entity.HomeServer

interface GetHomeServerUseCase {
    fun execute(): HomeServer
}
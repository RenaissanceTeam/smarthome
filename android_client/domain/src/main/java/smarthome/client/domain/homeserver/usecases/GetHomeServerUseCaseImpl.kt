package smarthome.client.domain.homeserver.usecases

import smarthome.client.data.api.homeserver.HomeServerRepo
import smarthome.client.domain.api.homeserver.usecases.GetHomeServerUseCase

class GetHomeServerUseCaseImpl(
    private val repo: HomeServerRepo
) : GetHomeServerUseCase {
    override fun execute() = repo.get()
}
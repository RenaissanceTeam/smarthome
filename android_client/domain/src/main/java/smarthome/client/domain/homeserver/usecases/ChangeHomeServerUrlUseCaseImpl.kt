package smarthome.client.domain.homeserver.usecases

import smarthome.client.data.api.homeserver.HomeServerRepo
import smarthome.client.domain.api.homeserver.usecases.ChangeHomeServerUrlUseCase

class ChangeHomeServerUrlUseCaseImpl(
    private val repo: HomeServerRepo
) : ChangeHomeServerUrlUseCase {
    override fun execute(url: String) {
//        val newHomeServer = repo.runCatching { get() }.getOrDefault(
//            HomeServer()).copy(url = url)
//
//        repo.save(newHomeServer)
    }
}
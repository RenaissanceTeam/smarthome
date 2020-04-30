package smarthome.client.domain.homeserver.usecases

import smarthome.client.data.api.homeserver.HomeServerRepo
import smarthome.client.domain.api.auth.usecases.LogoutUseCase
import smarthome.client.domain.api.homeserver.usecases.ChangeHomeServerUrlUseCase
import smarthome.client.entity.HomeServer

class ChangeHomeServerUrlUseCaseImpl(
        private val repo: HomeServerRepo,
        private val logoutUseCase: LogoutUseCase
) : ChangeHomeServerUrlUseCase {
    override suspend fun execute(url: String): Boolean {
        val currentActive = repo.getCurrentActive()
        val savedWithSameUrl = repo.getByUrl(url)

        if (currentActive?.url == url) return false

        currentActive
                ?.let {
                    deactivate(it)
                    savedWithSameUrl?.let { activate(it) } ?: createAndSave(url)
                }
                ?: createAndSave(url)

        logoutUseCase.execute()
        return true
    }

    private suspend fun createAndSave(url: String) {
        repo.save(createNewServer(url))
    }

    private suspend fun activate(server: HomeServer) {
        repo.update(server.copy(active = true))
    }

    private suspend fun deactivate(server: HomeServer) {
        repo.update(server.copy(active = false))
    }

    private fun createNewServer(url: String) = HomeServer(url = url, active = true)
}
package smarthome.client.domain.homeserver.usecases

import smarthome.client.data.api.homeserver.HomeServerRepo
import smarthome.client.domain.api.homeserver.usecases.ChangeHomeServerUrlUseCase
import smarthome.client.entity.HomeServer
import smarthome.client.util.fold

class ChangeHomeServerUrlUseCaseImpl(
        private val repo: HomeServerRepo
) : ChangeHomeServerUrlUseCase {
    override suspend fun execute(url: String) {
        val currentActive = repo.getCurrentActive()
        val savedWithSameUrl = repo.getByUrl(url)

        if (currentActive?.url == url) return
        if (currentActive == null) {
            repo.save(createNewServer(url))
            return
        }

        deactivate(currentActive)

        if (savedWithSameUrl == null) {
            createNewServer(url).let { repo.save(it) }
        } else {
            activate(savedWithSameUrl)
        }
    }

    private suspend fun activate(server: HomeServer) {
        repo.update(server.copy(active = true))
    }

    private suspend fun deactivate(server: HomeServer) {
        repo.update(server.copy(active = false))
    }

    private fun createNewServer(url: String) = HomeServer(url = url, active = true)
}
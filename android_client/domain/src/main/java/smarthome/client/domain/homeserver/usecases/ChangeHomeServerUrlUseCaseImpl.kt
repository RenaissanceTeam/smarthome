package smarthome.client.domain.homeserver.usecases

import smarthome.client.data.api.homeserver.HomeServerRepo
import smarthome.client.domain.api.homeserver.usecases.ChangeHomeServerUrlUseCase
import smarthome.client.entity.HomeServer

class ChangeHomeServerUrlUseCaseImpl(
    private val repo: HomeServerRepo
) : ChangeHomeServerUrlUseCase {
    override suspend fun execute(url: String) {
        when  (val currentActive = repo.getCurrentActive()) {
            null -> saveNewHomeServer(url)
            else -> updateExistingHomeServer(currentActive, url)
        }
    }
    
    private suspend fun saveNewHomeServer(url: String) {
        repo.save(HomeServer(url = url, active = true))
    }
    
    private suspend fun updateExistingHomeServer(homeServer: HomeServer, url: String) {
        repo.update(homeServer.copy(url = url))
    }
}
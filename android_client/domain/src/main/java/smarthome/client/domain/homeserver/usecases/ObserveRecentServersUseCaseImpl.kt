package smarthome.client.domain.homeserver.usecases

import io.reactivex.Observable
import smarthome.client.data.api.homeserver.HomeServerRepo
import smarthome.client.domain.api.homeserver.usecases.ObserveRecentServersUseCase
import smarthome.client.entity.HomeServer

class ObserveRecentServersUseCaseImpl(
        private val repo: HomeServerRepo
) : ObserveRecentServersUseCase {
    override fun execute(): Observable<List<HomeServer>> {
        return repo.get().map { it.filterNot { it.active } }
    }
}
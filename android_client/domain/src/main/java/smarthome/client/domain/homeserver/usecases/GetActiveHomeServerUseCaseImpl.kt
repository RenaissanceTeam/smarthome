package smarthome.client.domain.homeserver.usecases

import io.reactivex.Observable
import smarthome.client.data.api.homeserver.HomeServerRepo
import smarthome.client.domain.api.homeserver.usecases.GetActiveHomeServerUseCase
import smarthome.client.entity.HomeServer
import smarthome.client.util.DataStatus

class GetActiveHomeServerUseCaseImpl(
    private val repo: HomeServerRepo
) : GetActiveHomeServerUseCase {
    override fun execute(): Observable<DataStatus<HomeServer>> {
        return repo.get()
            .map { DataStatus.from(it.find { servers -> servers.active }) }
    }
}
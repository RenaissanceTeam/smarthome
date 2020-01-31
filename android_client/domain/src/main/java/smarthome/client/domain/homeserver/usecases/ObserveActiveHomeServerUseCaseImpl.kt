package smarthome.client.domain.homeserver.usecases

import io.reactivex.Observable
import smarthome.client.data.api.homeserver.HomeServerRepo
import smarthome.client.domain.api.homeserver.usecases.ObserveActiveHomeServerUseCase
import smarthome.client.entity.HomeServer
import smarthome.client.util.DataStatus

class ObserveActiveHomeServerUseCaseImpl(
    private val repo: HomeServerRepo
) : ObserveActiveHomeServerUseCase {
    override fun execute(): Observable<DataStatus<HomeServer>> {
        return repo.get().map { DataStatus.from(it.find { servers -> servers.active }) }
    }
}
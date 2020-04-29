package smarthome.client.domain.api.homeserver.usecases

import io.reactivex.Observable
import smarthome.client.entity.HomeServer

interface ObserveRecentServersUseCase {
    fun execute(): Observable<List<HomeServer>>
}
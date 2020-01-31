package smarthome.client.domain.api.homeserver.usecases

import io.reactivex.Observable
import smarthome.client.entity.HomeServer
import smarthome.client.util.DataStatus

interface ObserveActiveHomeServerUseCase {
    fun execute(): Observable<DataStatus<HomeServer>>
}
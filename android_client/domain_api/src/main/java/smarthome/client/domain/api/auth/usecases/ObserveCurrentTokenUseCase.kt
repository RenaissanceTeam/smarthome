package smarthome.client.domain.api.auth.usecases

import io.reactivex.Observable
import smarthome.client.util.DataStatus

interface ObserveCurrentTokenUseCase {
    fun execute(): Observable<DataStatus<String>>
}
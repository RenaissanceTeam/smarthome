package smarthome.client.domain.api.conrollers.usecases

import io.reactivex.Observable
import smarthome.client.entity.Controller
import smarthome.client.util.DataStatus

interface ObserveControllerUseCase {
    fun execute(id: Long): Observable<DataStatus<Controller>>
}
package smarthome.client.domain.api.auth.usecases

import io.reactivex.Observable
import smarthome.client.entity.User
import smarthome.client.util.DataStatus

interface ObserveCurrentUserUseCase {
    fun execute(): Observable<DataStatus<User>>
}
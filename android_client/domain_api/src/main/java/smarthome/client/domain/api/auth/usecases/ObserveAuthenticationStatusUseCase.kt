package smarthome.client.domain.api.auth.usecases

import io.reactivex.Observable

interface ObserveAuthenticationStatusUseCase {
    fun execute(): Observable<Boolean>
}
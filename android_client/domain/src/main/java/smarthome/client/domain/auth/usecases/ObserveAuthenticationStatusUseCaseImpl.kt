package smarthome.client.domain.auth.usecases

import io.reactivex.Observable
import smarthome.client.domain.api.auth.usecases.ObserveAuthenticationStatusUseCase
import smarthome.client.util.DATA

class ObserveAuthenticationStatusUseCaseImpl(
    private val observeCurrentTokenUseCaseImpl: ObserveCurrentTokenUseCaseImpl
) : ObserveAuthenticationStatusUseCase {
    override fun execute(): Observable<Boolean> {
        return observeCurrentTokenUseCaseImpl.execute().map { it.status == DATA }
    }
}
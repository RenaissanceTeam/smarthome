package smarthome.client.domain.auth.usecases

import io.reactivex.Observable
import smarthome.client.domain.api.auth.usecases.ObserveAuthenticationStatusUseCase
import smarthome.client.domain.api.auth.usecases.ObserveCurrentTokenUseCase
import smarthome.client.util.DATA

class ObserveAuthenticationStatusUseCaseImpl(
    private val observeCurrentTokenUseCase: ObserveCurrentTokenUseCase
) : ObserveAuthenticationStatusUseCase {
    override fun execute(): Observable<Boolean> {
        return observeCurrentTokenUseCase.execute().map { it.status == DATA }
    }
}
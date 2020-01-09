package smarthome.client.domain.auth.usecases

import io.reactivex.Observable
import smarthome.client.data.api.auth.TokenRepo
import smarthome.client.domain.api.auth.usecases.ObserveCurrentTokenUseCase
import smarthome.client.util.DataStatus

class ObserveCurrentTokenUseCaseImpl(
    private val tokenRepo: TokenRepo
) : ObserveCurrentTokenUseCase {
    override fun execute(): Observable<DataStatus<String>> {
        return tokenRepo.observe()
    }
}
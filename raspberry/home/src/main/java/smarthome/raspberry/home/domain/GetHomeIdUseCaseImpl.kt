package smarthome.raspberry.home.domain

import io.reactivex.Observable
import smarthome.raspberry.home.api.domain.GetHomeIdUseCase

class GetHomeIdUseCaseImpl(): GetHomeIdUseCase {
    override fun execute(): Observable<String> {
        TODO()
    }
}
package smarthome.raspberry.controllers.domain

import io.reactivex.Observable
import org.springframework.stereotype.Component
import smarthome.raspberry.controllers.api.domain.ObserveControllerStatesUseCase

@Component
class ObserveControllerStatesUseCaseImpl : ObserveControllerStatesUseCase {
    override fun execute(id: Long): Observable<String> {
        TODO()
    }
}
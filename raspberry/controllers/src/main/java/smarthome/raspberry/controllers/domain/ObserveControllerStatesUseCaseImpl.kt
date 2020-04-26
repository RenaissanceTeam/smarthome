package smarthome.raspberry.controllers.domain

import io.reactivex.Observable
import org.springframework.stereotype.Component
import smarthome.raspberry.controllers.api.domain.ObserveControllerStatesUseCase
import smarthome.raspberry.controllers.data.ControllersRepo
import smarthome.raspberry.controllers.data.ObservableControllersRepo
import smarthome.raspberry.core.toOptional
import java.util.*

@Component
class ObserveControllerStatesUseCaseImpl(
        private val controllersRepo: ObservableControllersRepo
) : ObserveControllerStatesUseCase {
    override fun execute(id: Long): Observable<Optional<String>> {
        return controllersRepo.observeById(id).map { it.flatMap { it.state.toOptional() } }
    }
}
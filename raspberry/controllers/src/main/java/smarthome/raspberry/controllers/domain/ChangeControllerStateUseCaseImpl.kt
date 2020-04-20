package smarthome.raspberry.controllers.domain

import org.springframework.stereotype.Component
import smarthome.raspberry.controllers.api.domain.ChangeControllerStateUseCase
import smarthome.raspberry.controllers.data.ControllersRepo
import smarthome.raspberry.entity.controller.Controller

@Component
class ChangeControllerStateUseCaseImpl(
        private val repo: ControllersRepo
) : ChangeControllerStateUseCase {
    override fun execute(controller: Controller, newState: String): Controller {
        return repo.save(controller.copy(state = newState)).blockingGet()
    }
}
package smarthome.raspberry.controllers.domain

import org.springframework.stereotype.Component
import smarthome.raspberry.controllers.api.domain.SaveControllerUseCase
import smarthome.raspberry.controllers.data.ObservableControllersRepo
import smarthome.raspberry.entity.controller.Controller

@Component
class SaveControllerUseCaseImpl(
        private val repo: ObservableControllersRepo
) : SaveControllerUseCase {
    override fun execute(controller: Controller): Controller = repo.save(controller)
}
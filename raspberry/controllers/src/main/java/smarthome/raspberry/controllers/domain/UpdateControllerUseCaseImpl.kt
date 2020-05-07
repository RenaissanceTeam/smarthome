package smarthome.raspberry.controllers.domain

import org.springframework.stereotype.Component
import smarthome.raspberry.controllers.api.domain.GetControllerByIdUseCase
import smarthome.raspberry.controllers.api.domain.UpdateControllerUseCase
import smarthome.raspberry.controllers.data.ObservableControllersRepo
import smarthome.raspberry.entity.controller.Controller

@Component
class UpdateControllerUseCaseImpl(
        private val getControllerByIdUseCase: GetControllerByIdUseCase,
        private val observableControllersRepo: ObservableControllersRepo
) : UpdateControllerUseCase {
    override fun execute(id: Long, partialUpdate: (Controller) -> Controller): Controller {
        val controller = getControllerByIdUseCase.execute(id)

        return observableControllersRepo.save(partialUpdate(controller))
    }
}
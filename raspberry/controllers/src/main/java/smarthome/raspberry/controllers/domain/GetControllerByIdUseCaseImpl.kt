package smarthome.raspberry.controllers.domain

import org.springframework.stereotype.Component
import smarthome.raspberry.controllers.api.domain.GetControllerByIdUseCase
import smarthome.raspberry.controllers.api.domain.NoControllerException
import smarthome.raspberry.controllers.data.ControllersRepo
import smarthome.raspberry.entity.controller.Controller

@Component
open class GetControllerByIdUseCaseImpl(
        private val controllersRepo: ControllersRepo
) : GetControllerByIdUseCase {
    override fun execute(id: Long): Controller {
        return controllersRepo.findById(id).runCatching { get() }.getOrElse { throw NoControllerException() }
    }
}
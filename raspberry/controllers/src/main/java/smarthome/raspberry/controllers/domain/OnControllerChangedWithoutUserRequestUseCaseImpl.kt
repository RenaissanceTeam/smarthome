package smarthome.raspberry.controllers.domain

import smarthome.library.common.BaseController
import smarthome.raspberry.controllers_api.data.ControllersRepository
import smarthome.raspberry.controllers_api.domain.OnControllerChangedWithoutUserRequestUseCase

class OnControllerChangedWithoutUserRequestUseCaseImpl(
        private val repository: ControllersRepository
) : OnControllerChangedWithoutUserRequestUseCase {

    override suspend fun execute(controller: BaseController) {
        repository.onControllerChanged(controller)
    }
}

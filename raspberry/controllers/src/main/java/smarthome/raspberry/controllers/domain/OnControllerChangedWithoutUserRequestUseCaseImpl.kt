package smarthome.raspberry.controllers.domain

import smarthome.library.common.BaseController
import smarthome.raspberry.controllers.data.ControllersRepository
import smarthome.raspberry.controllers.api.domain.OnControllerChangedWithoutUserRequestUseCase

class OnControllerChangedWithoutUserRequestUseCaseImpl(
        private val repository: ControllersRepository
) : OnControllerChangedWithoutUserRequestUseCase {

    override suspend fun execute(controller: BaseController) {
        // todo react to controller change
    }
}

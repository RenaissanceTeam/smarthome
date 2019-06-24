package smarthome.raspberry.domain.usecases

import smarthome.library.common.BaseController
import smarthome.library.common.ControllerState
import smarthome.raspberry.domain.HomeRepository

class ControllersUseCase(private val repository: HomeRepository) {

    suspend fun setNewState(controller: BaseController, state: ControllerState) {
        repository.setControllerState(controller, state)
        // todo check if someone is listening for updates of this controller
    }

    suspend fun fetchState(controller: BaseController ): ControllerState {
        // todo check if someone is listening for updates of this controller
        return repository.fetchControllerState(controller)
    }

    /**
     * Controller is changed without users request. It may be caused by environment changes,
     * scheduled script, etc.
     */
    suspend fun notifyControllerChanged(controller: BaseController) {
        repository.onControllerChanged(controller)
    }
}

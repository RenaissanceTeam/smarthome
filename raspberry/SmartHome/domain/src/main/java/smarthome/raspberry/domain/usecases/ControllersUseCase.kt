package smarthome.raspberry.domain.usecases

import smarthome.library.common.BaseController
import smarthome.raspberry.domain.HomeRepository

class ControllersUseCase(private val repository: HomeRepository) {
    /**
     * Controller is changed without user's request.
     *
     * It may be caused by environment changes, scheduled script, etc.
     */
    suspend fun notifyControllerChanged(controller: BaseController) {
        repository.onControllerChanged(controller)
    }
}

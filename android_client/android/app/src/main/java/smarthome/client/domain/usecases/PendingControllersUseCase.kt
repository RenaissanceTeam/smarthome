package smarthome.client.domain.usecases

import smarthome.client.domain.HomeRepository
import smarthome.library.common.BaseController

class PendingControllersUseCase(private val repository: HomeRepository) {
    fun getPendingController(controllerGuid: Long): BaseController {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
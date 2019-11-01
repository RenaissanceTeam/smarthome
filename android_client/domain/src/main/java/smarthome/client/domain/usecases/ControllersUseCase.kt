package smarthome.client.domain.usecases

import smarthome.client.data_api.HomeRepository
import smarthome.client.domain_api.NoControllerException
import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice

class ControllersUseCase(private val repository: HomeRepository) {
    suspend fun getController(controllerGuid: Long): BaseController {
        val controllers = repository.getControllers()

        return controllers.find { it.guid == controllerGuid } ?: throw NoControllerException(controllerGuid)
    }

    fun findController(devices: MutableList<IotDevice>, controllerGuid: Long): BaseController {
        for (device in devices) {
            val controllers = device.controllers
            return controllers.find { it.guid == controllerGuid } ?: continue
        }

        throw NoControllerException(controllerGuid)
    }

    suspend fun getControllers(): List<BaseController> {
        return repository.getControllers()
    }
}
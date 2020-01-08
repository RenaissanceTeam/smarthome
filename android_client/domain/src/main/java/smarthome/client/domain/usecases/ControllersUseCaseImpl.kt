package smarthome.client.domain.usecases

import smarthome.client.data.api.home.HomeRepository
import smarthome.client.domain.api.entity.Controller
import smarthome.client.domain.api.usecase.ControllersUseCase

class ControllersUseCaseImpl(private val repository: HomeRepository) : ControllersUseCase {
    override suspend fun getController(id: Long): Controller {
        val controllers = repository.getControllers()
        
        return controllers.find { it.id == id } ?: TODO()
    }
    
    override suspend fun getControllers(): List<Controller> {
        return repository.getControllers()
    }
}
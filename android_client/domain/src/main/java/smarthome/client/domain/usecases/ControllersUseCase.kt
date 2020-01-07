package smarthome.client.domain.usecases

import smarthome.client.data_api.home.HomeRepository
import smarthome.client.domain_api.entity.Controller

class ControllersUseCase(private val repository: HomeRepository) {
    suspend fun getController(id: Long): Controller {
        val controllers = repository.getControllers()
        
        return controllers.find { it.id == id } ?: TODO()
    }
    
    suspend fun getControllers(): List<Controller> {
        return repository.getControllers()
    }
}
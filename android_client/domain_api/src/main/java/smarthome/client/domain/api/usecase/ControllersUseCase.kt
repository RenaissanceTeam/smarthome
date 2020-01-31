package smarthome.client.domain.api.usecase

import smarthome.client.entity.Controller

interface ControllersUseCase {
    suspend fun getController(id: Long): Controller
    
    suspend fun getControllers(): List<Controller>
}
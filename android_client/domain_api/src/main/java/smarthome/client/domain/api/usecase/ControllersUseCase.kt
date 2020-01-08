package smarthome.client.domain.api.usecase

import smarthome.client.domain.api.entity.Controller

interface ControllersUseCase {
    suspend fun getController(id: Long): Controller
    
    suspend fun getControllers(): List<Controller>
}
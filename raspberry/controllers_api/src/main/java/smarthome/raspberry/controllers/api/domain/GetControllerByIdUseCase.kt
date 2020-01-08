package smarthome.raspberry.controllers.api.domain

import smarthome.raspberry.entity.Controller

interface GetControllerByIdUseCase {
    suspend fun execute(id: Long): Controller
}
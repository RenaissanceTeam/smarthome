package smarthome.client.presentation.scripts

import smarthome.client.domain.api.entity.Controller

interface ControllersProvider {
    suspend fun getControllers(): List<Controller>
}
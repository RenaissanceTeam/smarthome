package smarthome.client.presentation.scripts

import smarthome.client.entity.Controller

interface ControllersProvider {
    suspend fun getControllers(): List<Controller>
}
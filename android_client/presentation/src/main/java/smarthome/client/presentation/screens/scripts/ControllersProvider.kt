package smarthome.client.presentation.screens.scripts

import smarthome.library.common.BaseController

interface ControllersProvider {
    suspend fun getControllers(): List<BaseController>
}
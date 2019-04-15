package smarthome.client.scripts.conditions

import smarthome.library.common.BaseController

interface ControllerConditionProvider {
    suspend fun getControllers(): List<BaseController>
}
package smarthome.raspberry.data

import smarthome.library.common.BaseController

interface RemoteStorage {
    suspend fun init()
    fun onControllerChanged(controller: BaseController)
}
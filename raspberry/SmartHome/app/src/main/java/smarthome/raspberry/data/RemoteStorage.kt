package smarthome.raspberry.data

import smarthome.library.common.BaseController

interface RemoteStorage {
    suspend fun init()
    suspend fun onControllerChanged(controller: BaseController)
    suspend fun createHome(homeId: String)
    suspend fun isHomeIdUnique(homeId: String): Boolean
}
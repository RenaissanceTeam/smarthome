package smarthome.client.data.api.controllers

import io.reactivex.Observable
import smarthome.client.entity.Controller
import smarthome.client.util.DataStatus

interface ControllersRepo {
    fun observe(id: Long): Observable<DataStatus<Controller>>
    fun get(id: Long): Controller?
    suspend fun fetch(id: Long): Controller
    suspend fun setState(controllerId: Long, state: String): String
    suspend fun readState(controllerId: Long): String
    fun controllerUpdated(controller: Controller)
    suspend fun updateName(id: Long, name: String)
}
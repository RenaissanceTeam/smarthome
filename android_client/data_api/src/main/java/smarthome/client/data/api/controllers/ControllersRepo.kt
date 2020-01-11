package smarthome.client.data.api.controllers

import io.reactivex.Observable
import smarthome.client.entity.Controller
import smarthome.client.util.DataStatus

interface ControllersRepo {
    fun observe(id: Long): Observable<DataStatus<Controller>>
    suspend fun get(id: Long): Controller
}
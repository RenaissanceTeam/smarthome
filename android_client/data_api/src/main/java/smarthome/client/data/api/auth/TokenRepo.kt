package smarthome.client.data.api.auth

import io.reactivex.Observable
import smarthome.client.util.DataStatus

interface TokenRepo {
    fun observe(): Observable<DataStatus<String>>
    fun save(token: String)
    fun getCurrent(): DataStatus<String>
    fun clear()
}
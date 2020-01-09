package smarthome.client.data.api.auth

import io.reactivex.Observable
import smarthome.client.util.DataStatus

interface TokenRepo {
    fun observe(): Observable<DataStatus<String>>
}
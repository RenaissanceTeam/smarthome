package smarthome.client.data.auth

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import smarthome.client.data.api.auth.TokenRepo
import smarthome.client.util.DataStatus

class TokenRepoImpl : TokenRepo {
    private val token = BehaviorSubject.create<DataStatus<String>>()
    
    override fun observe() = token
}
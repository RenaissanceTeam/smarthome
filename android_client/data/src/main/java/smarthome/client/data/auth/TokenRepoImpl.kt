package smarthome.client.data.auth

import io.reactivex.subjects.BehaviorSubject
import smarthome.client.data.api.auth.TokenRepo
import smarthome.client.util.Data
import smarthome.client.util.DataStatus
import smarthome.client.util.EmptyStatus

class TokenRepoImpl : TokenRepo {
    private val default = EmptyStatus<String>()
    private val token = BehaviorSubject.createDefault<DataStatus<String>>(default)
    
    override fun observe() = token
    
    override fun save(token: String) {
        this.token.onNext(Data(token))
    }
    
    override fun getCurrent(): DataStatus<String> = token.value ?: default

    override fun clear() {
        token.onNext(EmptyStatus())
    }
}
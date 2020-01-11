package smarthome.client.data.auth

import io.reactivex.subjects.BehaviorSubject
import smarthome.client.data.api.auth.TokenRepo
import smarthome.client.util.DataStatus
import smarthome.client.util.EMPTY

class TokenRepoImpl : TokenRepo {
    private val default = DataStatus<String>(null, EMPTY)
    private val token = BehaviorSubject.createDefault(default)
    
    override fun observe() = token
    
    override fun save(token: String) {
        this.token.onNext(DataStatus.from(token))
    }
    
    override fun getCurrent(): DataStatus<String> = token.value ?: default
}
package smarthome.raspberry.authentication.data

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import smarthome.raspberry.authentication.api.domain.AuthStatus
import smarthome.raspberry.authentication.api.domain.entity.Credentials
import smarthome.raspberry.authentication.api.domain.User
import smarthome.raspberry.authentication.api.domain.exceptions.NotSignedInException
import smarthome.raspberry.authentication.data.command.SignInCommand
import smarthome.raspberry.authentication.data.command.SignOutCommand
import smarthome.raspberry.authentication.data.query.GetUserQuery

class AuthRepoImpl(

) : AuthRepo {
    private val authStatus = BehaviorSubject.create<AuthStatus>()
    private val user = BehaviorSubject.create<User>()
    
    override fun getUser() = user.value ?: throw NotSignedInException()
    
    override fun hasUser() = authStatus.value == AuthStatus.SIGNED_IN
    
    override fun getAuthStatus() = authStatus
    
    override fun getUserId(): Observable<String> = user.map { it.id }
    
    override fun checkUserExists(login: String): Boolean {
        TODO()
    }
}
package smarthome.raspberry.authentication.data

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import smarthome.raspberry.authentication.api.domain.AuthStatus
import smarthome.raspberry.authentication.api.domain.Credentials
import smarthome.raspberry.authentication.api.domain.User
import smarthome.raspberry.authentication.api.domain.exceptions.NotSignedInException
import smarthome.raspberry.authentication.data.command.SignInCommand
import smarthome.raspberry.authentication.data.command.SignOutCommand
import smarthome.raspberry.authentication.data.query.GetUserQuery

class AuthRepoImpl(
    private val signInCommand: SignInCommand,
    private val signOutCommand: SignOutCommand,
    getUserQuery: GetUserQuery
) : AuthRepo {
    private val authStatus = BehaviorSubject.create<AuthStatus>()
    private val user = BehaviorSubject.create<User>()
    
    init {
        try {
            getUserQuery.execute().apply {
                authStatus.onNext(AuthStatus.SIGNED_IN)
                user.onNext(this)
            }
        } catch (e: NotSignedInException) {
            authStatus.onNext(AuthStatus.NOT_SIGNED_IN)
        }
    }
    
    override fun getUser() = user.value ?: throw NotSignedInException()
    
    override fun hasUser() = authStatus.value == AuthStatus.SIGNED_IN
    
    override fun getAuthStatus() = authStatus
    
    override fun getUserId(): Observable<String> = user.map { it.id }
    
    override suspend fun signIn(credential: Credentials): User {
        return signInCommand.execute(credential).apply {
            authStatus.onNext(AuthStatus.SIGNED_IN)
            user.onNext(this)
        }
    }

    override suspend fun signOut() {
        signOutCommand.execute()
        authStatus.onNext(AuthStatus.NOT_SIGNED_IN)
    }
}
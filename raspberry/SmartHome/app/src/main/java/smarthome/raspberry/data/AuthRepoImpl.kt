package smarthome.raspberry.data

import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import smarthome.raspberry.NoAuthentication
import smarthome.raspberry.domain.AuthRepo
import smarthome.raspberry.domain.usecases.AuthUseCase

class AuthRepoImpl : AuthRepo {
    private val authStatus = PublishSubject.create<AuthUseCase.AuthStatus>()

    init {
        FirebaseAuth.getInstance().addAuthStateListener {
            if (it.currentUser != null) authStatus.onNext(AuthUseCase.AuthStatus.SIGNED_IN)
            else authStatus.onNext(AuthUseCase.AuthStatus.NOT_SIGNED_IN)
        }
    }

    override fun getAuthStatus(): Observable<AuthUseCase.AuthStatus> {
        return authStatus
    }

    override fun getUserId(): String {
        return FirebaseAuth.getInstance().currentUser?.uid ?: throw NoAuthentication()
    }
}
package smarthome.raspberry.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import smarthome.raspberry.domain.AuthRepo
import smarthome.raspberry.domain.usecases.AuthUseCase

class AuthRepoImpl : AuthRepo {
    private val authStatus = BehaviorSubject.create<AuthUseCase.AuthStatus>()
    private val userId = BehaviorSubject.create<String>()

    init {
        FirebaseAuth.getInstance().addAuthStateListener {


            val currentUser = it.currentUser
            if (currentUser != null) {
                authStatus.onNext(AuthUseCase.AuthStatus.SIGNED_IN)
                userId.onNext(currentUser.uid)
            } else {
                authStatus.onNext(AuthUseCase.AuthStatus.NOT_SIGNED_IN)
            }
        }
    }

    override fun getAuthStatus(): Observable<AuthUseCase.AuthStatus> {
        return authStatus
    }

    override fun getUserId(): Observable<String> {
        return userId
    }
}
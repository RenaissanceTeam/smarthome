package smarthome.raspberry.data

import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import smarthome.raspberry.authentication_api.AuthRepo
import smarthome.raspberry.authentication.AuthUseCase

class AuthRepoImpl : smarthome.raspberry.authentication_api.AuthRepo {
    private val authStatus = BehaviorSubject.create<smarthome.raspberry.authentication.AuthUseCase.AuthStatus>()
    private val userId = BehaviorSubject.create<String>()

    init {
        FirebaseAuth.getInstance().addAuthStateListener {


            val currentUser = it.currentUser
            if (currentUser != null) {
                authStatus.onNext(smarthome.raspberry.authentication.AuthUseCase.AuthStatus.SIGNED_IN)
                userId.onNext(currentUser.uid)
            } else {
                authStatus.onNext(smarthome.raspberry.authentication.AuthUseCase.AuthStatus.NOT_SIGNED_IN)
            }
        }
    }

    override fun getAuthStatus(): Observable<smarthome.raspberry.authentication.AuthUseCase.AuthStatus> {
        return authStatus
    }

    override fun getUserId(): Observable<String> {
        return userId
    }
}
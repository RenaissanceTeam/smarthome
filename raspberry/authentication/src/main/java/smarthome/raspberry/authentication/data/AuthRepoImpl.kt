package smarthome.raspberry.authentication.data

import com.google.firebase.auth.FirebaseAuth
import io.reactivex.subjects.BehaviorSubject
import smarthome.raspberry.authentication.api.data.AuthRepo
import smarthome.raspberry.authentication.api.domain.AuthStatus

class AuthRepoImpl : AuthRepo {
    private val authStatus = BehaviorSubject.create<AuthStatus>()
    private val userId = BehaviorSubject.create<String>()

    init {
        FirebaseAuth.getInstance().addAuthStateListener {
            val currentUser = it.currentUser
            if (currentUser != null) {
                authStatus.onNext(AuthStatus.SIGNED_IN)
                userId.onNext(currentUser.uid)
            } else {
                authStatus.onNext(AuthStatus.NOT_SIGNED_IN)
            }
        }
    }

    override fun getAuthStatus() = authStatus
    override fun getUserId() = userId
}
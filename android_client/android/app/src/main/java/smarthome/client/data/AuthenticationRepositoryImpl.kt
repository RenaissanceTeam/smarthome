package smarthome.client.data

import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import smarthome.client.auth.AuthUIWrapper
import smarthome.client.domain.AuthenticationRepository

class AuthenticationRepositoryImpl(private val firebaseAuth: FirebaseAuth): AuthenticationRepository {
    private val isAuthenticated: BehaviorSubject<Boolean> = BehaviorSubject.create()
    private val userEmail: BehaviorSubject<String> = BehaviorSubject.create()

    init {
        onNewAuth()
    }

    fun onNewAuth() {
        isAuthenticated.onNext(firebaseAuth.currentUser != null)
        userEmail.onNext(firebaseAuth.currentUser?.email ?: "Not authenticated")
    }

    override fun getAuthenticationStatus(): Observable<Boolean> {
        return isAuthenticated
    }
}
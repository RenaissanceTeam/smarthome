package smarthome.client.data

import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import smarthome.client.domain.AuthenticationRepository

class AuthenticationRepositoryImpl(private val firebaseAuth: FirebaseAuth): AuthenticationRepository {
    private val isAuthenticated: BehaviorSubject<Boolean> = BehaviorSubject.create()
    private val userEmail: BehaviorSubject<String> = BehaviorSubject.create()

    init {
        onNewAuth()
    }

    private fun onNewAuth() {
        isAuthenticated.onNext(firebaseAuth.currentUser != null)
        userEmail.onNext(firebaseAuth.currentUser?.email ?: "Not authenticated")
    }

    override fun getAuthenticationStatus(): Observable<Boolean> {
        return isAuthenticated
    }

    override fun getEmail(): Observable<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setAuthenticationStatus(isAuthenticated: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun resetEmail() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateEmail() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
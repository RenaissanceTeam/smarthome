package smarhome.client.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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
        return userEmail
    }

    override suspend fun setAuthenticationStatus(isAuthenticated: Boolean) {
        this.isAuthenticated.onNext(isAuthenticated)
    }

    override suspend fun resetEmail() {
        userEmail.onNext("")
    }

    override suspend fun updateEmail() {
        userEmail.onNext(getCurrentUser().email ?: "")
    }

    private fun getCurrentUser(): FirebaseUser {
        return firebaseAuth.currentUser ?: throw RuntimeException("current user is null")
    }

    override suspend fun getUserId(): String? {
        return firebaseAuth.currentUser?.uid
    }
}
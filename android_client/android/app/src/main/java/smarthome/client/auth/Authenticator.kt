package smarthome.client.auth

import com.google.firebase.auth.FirebaseAuth
import io.reactivex.subjects.ReplaySubject

object Authenticator {
    private val firebaseAuth = FirebaseAuth.getInstance()
    val isAuthenticated: ReplaySubject<Boolean> = ReplaySubject.createWithSize(1)
    val userEmail: ReplaySubject<String> = ReplaySubject.createWithSize(1)

    init {
        onNewAuth()
    }

    fun onNewAuth() {
        isAuthenticated.onNext(firebaseAuth.currentUser != null)
        userEmail.onNext(firebaseAuth.currentUser?.email ?: "Not authenticated")
    }

    fun getUserId(): String? {
        return firebaseAuth.currentUser?.uid
    }

    suspend fun signOut() {
        AuthUIWrapper.signOut()
        onNewAuth()
    }
}
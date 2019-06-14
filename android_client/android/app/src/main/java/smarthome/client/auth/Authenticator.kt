package smarthome.client.auth

import com.google.firebase.auth.FirebaseAuth
import io.reactivex.subjects.BehaviorSubject

object Authenticator {
    private val firebaseAuth = FirebaseAuth.getInstance()
    val isAuthenticated: BehaviorSubject<Boolean> = BehaviorSubject.create()
    val userEmail: BehaviorSubject<String> = BehaviorSubject.create()

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
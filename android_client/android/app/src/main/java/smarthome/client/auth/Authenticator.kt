package smarthome.client.auth

import com.google.firebase.auth.FirebaseAuth

class Authenticator(val firebaseAuth: FirebaseAuth) {
    fun isAuthenticated(): Boolean {
        return firebaseAuth.currentUser != null
    }

    fun getUserId(): String? {
        return firebaseAuth.currentUser?.uid
    }
}
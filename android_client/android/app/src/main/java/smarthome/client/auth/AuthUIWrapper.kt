package smarthome.client.auth

import android.content.Intent
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import smarthome.client.App

object AuthUIWrapper {
    val providers = listOf(AuthUI.IdpConfig.GoogleBuilder().build())

    fun getAuthIntent(): Intent {
        return AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .build()
    }

    suspend fun signOut() {
        FirebaseAuth.getInstance().signOut()
        AuthUI.getInstance().delete(App.appContext)
        AuthUI.getInstance().signOut(App.appContext)
    }
}
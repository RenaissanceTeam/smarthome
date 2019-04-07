package smarthome.client.auth

import android.content.Intent
import com.firebase.ui.auth.AuthUI
import smarthome.client.App

object AuthUIWrapper {
    val providers = listOf(AuthUI.IdpConfig.GoogleBuilder().build())

    fun clearCredentials() = AuthUI.getInstance().delete(App.appContext)

    fun getAuthIntent(): Intent {
        return AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build()
    }
}
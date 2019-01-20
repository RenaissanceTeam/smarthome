package ru.smarthome.auth

import android.content.Intent
import com.firebase.ui.auth.AuthUI

object AuthUIWrapper {
    val providers = listOf(AuthUI.IdpConfig.GoogleBuilder().build())

    fun getAuthIntent(): Intent {
        return AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build()
    }
}
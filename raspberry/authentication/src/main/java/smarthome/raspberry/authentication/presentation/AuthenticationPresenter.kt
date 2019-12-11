package smarthome.raspberry.authentication.presentation

import androidx.lifecycle.LifecycleObserver
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import smarthome.raspberry.authentication.api.domain.Credentials

interface AuthenticationPresenter : LifecycleObserver {
    fun signIn()
    fun sighOut()
    fun deleteAll()
    fun onAuthenticationSuccess(credentials: Credentials)
    fun onAuthenticationFail()
}
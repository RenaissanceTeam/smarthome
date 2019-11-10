package smarthome.raspberry.authentication.presentation

import androidx.lifecycle.LifecycleObserver
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

interface AuthenticationPresenter : LifecycleObserver {
    fun signIn()
    fun sighOut()
    fun deleteAll()
    fun onAuthenticationSuccess(account: GoogleSignInAccount)
    fun onAuthenticationFail()
}
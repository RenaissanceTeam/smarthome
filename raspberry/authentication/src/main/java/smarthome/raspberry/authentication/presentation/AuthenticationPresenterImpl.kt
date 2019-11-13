package smarthome.raspberry.authentication.presentation

import android.annotation.SuppressLint
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import smarthome.raspberry.authentication.api.domain.AuthStatus
import smarthome.raspberry.authentication.api.domain.GetAuthStatusUseCase
import smarthome.raspberry.authentication.api.domain.GetUserInfoUseCase
import smarthome.raspberry.authentication.domain.internal.AuthenticateWithFirebaseUseCase
import smarthome.raspberry.authentication.domain.internal.SignOutOfFirebaseUseCase
import smarthome.raspberry.home.api.domain.ClearHomeInfoUseCase
import smarthome.raspberry.home.api.presentation.MainFlowLauncher

class AuthenticationPresenterImpl(
        private val view: AuthenticationView,
        private val authenticateWithFirebaseUseCase: AuthenticateWithFirebaseUseCase,
        private val signOutOfFirebaseUseCase: SignOutOfFirebaseUseCase,
        private val mainFlowLauncher: MainFlowLauncher,
        private val clearHomeInfoUseCase: ClearHomeInfoUseCase,
        private val getAuthStatusUseCase: GetAuthStatusUseCase,
        private val getUserInfoUseCase: GetUserInfoUseCase
) : AuthenticationPresenter {
    private val scope = CoroutineScope(Dispatchers.Main)

    @SuppressLint("CheckResult")
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        getAuthStatusUseCase.execute()
                .subscribe {
                    when (it) {
                        AuthStatus.SIGNED_IN -> view.setState(
                                AuthenticationState.Authenticated(
                                        getUserInfoUseCase.execute()
                                ))
                        AuthStatus.NOT_SIGNED_IN -> view.setState(
                                AuthenticationState.Empty)
                        else -> {}
                    }
                }
    }

    override fun signIn() {
        view.startAuthentication()
    }

    override fun sighOut() {
        scope.launch {
            signOutOfFirebaseUseCase.execute()
        }
    }

    override fun deleteAll() {
        view.showDeleteAllConfirmationDialog {
            scope.launch {
                clearHomeInfoUseCase.execute()
                signOutOfFirebaseUseCase.execute()
            }
        }
    }

    override fun onAuthenticationSuccess(account: GoogleSignInAccount) {
        view.setState(AuthenticationState.Loading)

        val credential =
                GoogleAuthProvider.getCredential(account.idToken, null)
        scope.launch {
            try {
                authenticateWithFirebaseUseCase.execute(credential)
                mainFlowLauncher.launch()
            } catch (e: Throwable) {
                view.setState(
                        AuthenticationState.Error(
                                e.message.orEmpty()))
            }
        }
    }

    override fun onAuthenticationFail() {
        view.setState(AuthenticationState.Error(
                "Authentication fail"))
    }
}
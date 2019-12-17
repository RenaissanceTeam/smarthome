package smarthome.raspberry.authentication.presentation

import android.annotation.SuppressLint
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import smarthome.raspberry.authentication.api.domain.AuthStatus
import smarthome.raspberry.authentication.api.domain.Credentials
import smarthome.raspberry.authentication.api.domain.GetAuthStatusUseCase
import smarthome.raspberry.authentication.api.domain.GetUserInfoUseCase
import smarthome.raspberry.authentication.domain.internal.AuthenticateWithCredentialsUseCase
import smarthome.raspberry.authentication.domain.internal.SignOutOfFirebaseUseCase
import smarthome.raspberry.home.api.domain.ClearHomeInfoUseCase
import smarthome.raspberry.home.api.presentation.MainFlowLauncher

class AuthenticationPresenterImpl(
    private val view: AuthenticationView,
    private val authenticateWithCredentialsUseCase: AuthenticateWithCredentialsUseCase,
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

    override fun onAuthenticationSuccess(credentials: Credentials) {
        view.setState(AuthenticationState.Loading)

        scope.launch {
            try {
                authenticateWithCredentialsUseCase.execute(credentials)
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
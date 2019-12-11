package smarthome.raspberry.authentication.data

import com.google.android.gms.auth.api.Auth
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.reactivex.subjects.BehaviorSubject
import smarthome.raspberry.authentication.api.domain.AuthStatus
import smarthome.raspberry.authentication.api.domain.Credentials
import smarthome.raspberry.authentication.api.domain.User
import smarthome.raspberry.authentication.api.domain.exceptions.NotSignedInException
import smarthome.raspberry.authentication.data.command.SignInCommand
import smarthome.raspberry.authentication.data.mapper.FirebaseUserToUserMapper
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AuthRepoImpl(
        private val auth: FirebaseAuth,
        private val apiClient: GoogleApiClient,
        private val signInCommand: SignInCommand,
        private val userMapper: FirebaseUserToUserMapper
) : AuthRepo {
    private val authStatus = BehaviorSubject.create<AuthStatus>()
    private val userId = BehaviorSubject.create<String>()

    override fun getUser() = userMapper.map(auth.currentUser ?: throw NotSignedInException())
    override fun getAuthStatus() = authStatus
    override fun getUserId() = userId

    override suspend fun signIn(credential: Credentials): User {
        return signInCommand.execute(credential).apply {
            authStatus.onNext(AuthStatus.SIGNED_IN)
            userId.onNext(id)
        }
    }

    override suspend fun signOut() {
        auth.signOut()
        suspendCoroutine<Unit> { continuation ->
            Auth.GoogleSignInApi.signOut(apiClient)
                    .setResultCallback { continuation.resumeWith(Result.success(Unit)) }
        }

        authStatus.onNext(AuthStatus.NOT_SIGNED_IN)
    }
}
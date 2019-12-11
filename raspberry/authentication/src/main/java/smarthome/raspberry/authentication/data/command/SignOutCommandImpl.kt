package smarthome.raspberry.authentication.data.command

import com.google.android.gms.auth.api.Auth
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import kotlin.coroutines.suspendCoroutine

class SignOutCommandImpl (
    private val auth: FirebaseAuth,
    private val apiClient: GoogleApiClient
    )
: SignOutCommand {
    override suspend fun execute() {
        auth.signOut()
        suspendCoroutine<Unit> { continuation ->
            Auth.GoogleSignInApi.signOut(apiClient)
                .setResultCallback { continuation.resumeWith(Result.success(Unit)) }
        }
    }
}
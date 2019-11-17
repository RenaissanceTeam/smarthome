package smarthome.raspberry.authentication.domain.internal

import com.google.firebase.auth.AuthCredential
import smarthome.raspberry.authentication.api.domain.User
import smarthome.raspberry.authentication.data.AuthRepo

class AuthenticateWithFirebaseUseCaseImpl(
        private val repo: AuthRepo
) : AuthenticateWithFirebaseUseCase {

    override suspend fun execute(credential: AuthCredential): User {
        return repo.signIn(credential)
    }
}
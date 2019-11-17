package smarthome.raspberry.authentication.domain.internal

import com.google.firebase.auth.AuthCredential
import smarthome.raspberry.authentication.api.domain.User

interface AuthenticateWithFirebaseUseCase {
    suspend fun execute(credential: AuthCredential): User
}


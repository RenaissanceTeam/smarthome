package smarthome.raspberry.authentication.domain.internal

import smarthome.raspberry.authentication.data.AuthRepo

class SignOutOfFirebaseUseCaseImpl(
        private val repo: AuthRepo
) : SignOutOfFirebaseUseCase {
    override suspend fun execute() {
        repo.signOut()
    }
}
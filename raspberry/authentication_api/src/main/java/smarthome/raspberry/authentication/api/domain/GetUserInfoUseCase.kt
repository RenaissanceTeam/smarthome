package smarthome.raspberry.authentication.api.domain

interface GetUserInfoUseCase {
    fun execute(): User
}
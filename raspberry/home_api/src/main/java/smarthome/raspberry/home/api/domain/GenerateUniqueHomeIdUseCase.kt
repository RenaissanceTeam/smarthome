package smarthome.raspberry.home.api.domain

interface GenerateUniqueHomeIdUseCase {
    suspend fun execute(): String
}
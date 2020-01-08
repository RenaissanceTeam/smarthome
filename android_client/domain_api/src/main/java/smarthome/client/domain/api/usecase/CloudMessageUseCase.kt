package smarthome.client.domain.api.usecase

interface CloudMessageUseCase {
    suspend fun onNewToken(newToken: String?)
    
    suspend fun noSavedToken(): Boolean
}
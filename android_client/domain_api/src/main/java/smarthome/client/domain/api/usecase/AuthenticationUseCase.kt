package smarthome.client.domain.api.usecase

import io.reactivex.Observable

interface AuthenticationUseCase {
    fun getAuthenticationStatus(): Observable<Boolean>
    fun getEmail(): Observable<String>
    
    suspend fun onAuthFail()
    
    suspend fun onAuthSuccess()
    
    suspend fun signOut()
    
    suspend fun getUserId(): String?
}
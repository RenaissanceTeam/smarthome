package smarthome.library.common

import io.reactivex.Observable

interface InstanceTokenStorage {

    suspend fun saveToken(userId: String, token: String)
    suspend fun getToken(userId: String): String
    suspend fun removeToken(userId: String)
    fun observeTokenChanges(): Observable<String>
}
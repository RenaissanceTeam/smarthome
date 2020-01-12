package smarthome.client.data.retrofit

import io.reactivex.subjects.BehaviorSubject
import smarthome.client.domain.api.homeserver.usecases.ObserveActiveHomeServerUseCase
import smarthome.client.util.Data

class HomeServerUrlHolder(
    private val observeActiveHomeServerUseCase: ObserveActiveHomeServerUseCase
) {
    private val someSafeUrl = "http://localhost:80"
    
    private val serverUrl = BehaviorSubject.createDefault(someSafeUrl).apply {
        observeActiveHomeServerUseCase.execute().map {
            when (it) {
                is Data -> it.data.url
                else -> someSafeUrl
            }
        }.subscribe(this)
    }
    
    fun get(): String = serverUrl.value ?: someSafeUrl
}
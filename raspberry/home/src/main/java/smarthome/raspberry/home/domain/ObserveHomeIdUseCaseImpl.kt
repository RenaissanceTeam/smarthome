package smarthome.raspberry.home.domain

import io.reactivex.Observable
import smarthome.raspberry.home.api.domain.ObserveHomeIdUseCase
import smarthome.raspberry.home.data.HomeRepository
import smarthome.raspberry.home.data.storage.LocalStorage

class ObserveHomeIdUseCaseImpl(
    private val storage: LocalStorage
) : ObserveHomeIdUseCase {
    
    override fun execute(): Observable<String> = storage.getHomeId()
}
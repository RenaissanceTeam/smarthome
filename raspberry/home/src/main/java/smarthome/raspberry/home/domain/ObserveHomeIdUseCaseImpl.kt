package smarthome.raspberry.home.domain

import io.reactivex.Observable
import smarthome.raspberry.home.api.domain.ObserveHomeIdUseCase
import smarthome.raspberry.home.data.HomeRepository

class ObserveHomeIdUseCaseImpl(
    private val repository: HomeRepository
) : ObserveHomeIdUseCase {
    
    override fun execute(): Observable<String> = repository.getHomeId()
}
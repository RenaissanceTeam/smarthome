package smarthome.raspberry.home.domain

import io.reactivex.Observable
import smarthome.raspberry.home.api.domain.ObserveHomeIdUseCase
import smarthome.raspberry.home.data.HomeRepository
import smarthome.raspberry.util.persistence.StorageHelper
import smarthome.raspberry.util.persistence.observe

class ObserveHomeIdUseCaseImpl(private val repository: HomeRepository): ObserveHomeIdUseCase {
    override fun execute(): Observable<String> = repository.getHomeId()
    
    companion object {
        internal const val HOME_ID = "HOME_ID"
    }
}
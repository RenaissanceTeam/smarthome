package smarthome.raspberry.home.domain

import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.runBlocking
import smarthome.raspberry.home.api.domain.CreateHomeUseCase
import smarthome.raspberry.home.api.domain.eventbus.ObserveHomeEventsUseCase
import smarthome.raspberry.home.api.domain.eventbus.PublishEventUseCase
import smarthome.raspberry.home.api.domain.eventbus.events.HasHomeIdentifier
import smarthome.raspberry.home.api.domain.eventbus.events.NeedHomeIdentifier
import smarthome.raspberry.home.data.HomeRepository

class HomeStateResolver(
    private val publishEvent: PublishEventUseCase,
    getEventsUseCase: ObserveHomeEventsUseCase,
    private val repo: HomeRepository,
    private val createHomeUseCase: CreateHomeUseCase
) {
    
    init {
        getEventsUseCase.execute().observeOn(Schedulers.io()).subscribe {
            runBlocking {
                when (it) {
                    is NeedHomeIdentifier -> handleNeedHomeId()
                }
            }
        }
    }
    
    private suspend fun handleNeedHomeId() {
        when (repo.hasHomeId()) {
            true -> publishEvent.execute(HasHomeIdentifier())
            false -> createHomeUseCase.execute()
        }
    }
}
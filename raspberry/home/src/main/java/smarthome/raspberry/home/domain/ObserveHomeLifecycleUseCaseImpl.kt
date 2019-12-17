package smarthome.raspberry.home.domain

import io.reactivex.Observable
import smarthome.raspberry.home.api.domain.lifecycle.HomeLifecycleEvent
import smarthome.raspberry.home.api.domain.ObserveHomeEventsUseCase
import smarthome.raspberry.home.api.domain.lifecycle.ObserveHomeLifecycleUseCase

class ObserveHomeLifecycleUseCaseImpl(
    private val homeEvents: ObserveHomeEventsUseCase
) : ObserveHomeLifecycleUseCase {
    override fun execute(): Observable<HomeLifecycleEvent> {
        return homeEvents.execute()
            .filter { it is HomeLifecycleEvent }
            .map { it as HomeLifecycleEvent }
    }
}
package smarthome.raspberry.home.domain.eventbus

import io.reactivex.Observable
import smarthome.raspberry.home.api.domain.eventbus.events.HomeLifecycleEvent
import smarthome.raspberry.home.api.domain.eventbus.ObserveHomeEventsUseCase
import smarthome.raspberry.home.api.domain.eventbus.ObserveHomeLifecycleUseCase

class ObserveHomeLifecycleUseCaseImpl(
    private val homeEvents: ObserveHomeEventsUseCase
) : ObserveHomeLifecycleUseCase {
    override fun execute(): Observable<HomeLifecycleEvent> {
        return homeEvents.execute()
            .filter { it is HomeLifecycleEvent }
            .map { it as HomeLifecycleEvent }
    }
}
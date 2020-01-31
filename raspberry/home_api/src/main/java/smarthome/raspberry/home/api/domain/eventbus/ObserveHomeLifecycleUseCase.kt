package smarthome.raspberry.home.api.domain.eventbus

import io.reactivex.Observable
import smarthome.raspberry.home.api.domain.eventbus.events.HomeLifecycleEvent

interface ObserveHomeLifecycleUseCase {
    fun execute(): Observable<HomeLifecycleEvent>
}
package smarthome.raspberry.home.api.domain.lifecycle

import io.reactivex.Observable
import smarthome.raspberry.home.api.domain.lifecycle.HomeLifecycleEvent

interface ObserveHomeLifecycleUseCase {
    fun execute(): Observable<HomeLifecycleEvent>
}
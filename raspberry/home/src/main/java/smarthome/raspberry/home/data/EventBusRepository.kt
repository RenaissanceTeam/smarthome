package smarthome.raspberry.home.data

import io.reactivex.Observable
import smarthome.raspberry.home.api.domain.HomeEventBusEvent

interface EventBusRepository {
    fun getEvents(): Observable<HomeEventBusEvent>
    fun addEvent(event: HomeEventBusEvent)
}
package smarthome.raspberry.home.domain

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import smarthome.raspberry.home.api.domain.eventbus.events.Event
import smarthome.raspberry.home.api.domain.eventbus.events.HomeLifecycleEvent
import smarthome.raspberry.home.api.domain.eventbus.ObserveHomeEventsUseCase
import smarthome.raspberry.home.api.domain.eventbus.ObserveHomeLifecycleUseCase
import smarthome.raspberry.home.domain.eventbus.ObserveHomeLifecycleUseCaseImpl

class ObserveHomeLifecycleUseCaseImplTest {
    
    private lateinit var homeEvents: ObserveHomeEventsUseCase
    private lateinit var homeLifecycleUseCase: ObserveHomeLifecycleUseCase
    
    @Before
    fun setUp() {
        homeEvents = mock {}
        homeLifecycleUseCase =
            ObserveHomeLifecycleUseCaseImpl(
                homeEvents)
    }
    
    @Test
    fun `should filter for home lifecycle events`() {
        val observable = Observable.just(
            mock<HomeLifecycleEvent>(),
            mock<Event>(),
            mock<Event>(),
            mock<HomeLifecycleEvent>()
        )
        
        whenever(homeEvents.execute()).then { observable }
        
        homeLifecycleUseCase.execute().test().assertValueCount(2)
    }
}
package smarthome.raspberry.home.domain

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import smarthome.raspberry.home.api.domain.HomeEventBusEvent
import smarthome.raspberry.home.api.domain.lifecycle.HomeLifecycleEvent
import smarthome.raspberry.home.api.domain.ObserveHomeEventsUseCase
import smarthome.raspberry.home.api.domain.lifecycle.ObserveHomeLifecycleUseCase

class ObserveHomeLifecycleUseCaseImplTest {
    
    private lateinit var homeEvents: ObserveHomeEventsUseCase
    private lateinit var homeLifecycleUseCase: ObserveHomeLifecycleUseCase
    
    @Before
    fun setUp() {
        homeEvents = mock {}
        homeLifecycleUseCase = ObserveHomeLifecycleUseCaseImpl(homeEvents)
    }
    
    @Test
    fun `should filter for home lifecycle events`() {
        val observable = Observable.just(
            mock<HomeLifecycleEvent>(),
            mock<HomeEventBusEvent>(),
            mock<HomeEventBusEvent>(),
            mock<HomeLifecycleEvent>()
        )
        
        whenever(homeEvents.execute()).then { observable }
        
        homeLifecycleUseCase.execute().test().assertValueCount(2)
    }
}
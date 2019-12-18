package smarthome.raspberry.home.domain

import com.nhaarman.mockitokotlin2.argThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import smarthome.raspberry.home.api.domain.CreateHomeUseCase
import smarthome.raspberry.home.api.domain.eventbus.ObserveHomeEventsUseCase
import smarthome.raspberry.home.api.domain.eventbus.PublishEventUseCase
import smarthome.raspberry.home.api.domain.eventbus.events.Event
import smarthome.raspberry.home.api.domain.eventbus.events.HasHomeIdentifier
import smarthome.raspberry.home.api.domain.eventbus.events.NeedHomeIdentifier
import smarthome.raspberry.home.data.HomeRepository

class HomeStateResolverTest {
    private lateinit var publishEvent: PublishEventUseCase
    private lateinit var getEventsUseCase: ObserveHomeEventsUseCase
    private lateinit var events: PublishSubject<Event>
    private lateinit var createHomeUseCase: CreateHomeUseCase
    private lateinit var repo: HomeRepository
    private lateinit var resolver: HomeStateResolver
    
    @Before
    fun setUp() {
        publishEvent = mock {}
        getEventsUseCase = mock {
            on { execute() }.then { events }
        }
        events = PublishSubject.create()
        repo = mock {}
        createHomeUseCase = mock {}
        resolver = HomeStateResolver(publishEvent, getEventsUseCase, repo, createHomeUseCase)
    }
    
    @Test
    fun `when need home id and has saved should publish event has home id`() {
        whenever(repo.hasHomeId()).then { true }
        
        events.onNext(NeedHomeIdentifier())
        verify(publishEvent).execute(argThat { this is HasHomeIdentifier })
    }
    
    @Test
    fun `when need home id and there is no saved should execute create home id use case`() {
        whenever(repo.hasHomeId()).then { false }
    
        events.onNext(NeedHomeIdentifier())
        runBlocking { verify(createHomeUseCase).execute() }
    }
}
package smarthome.raspberry.home.domain

import com.nhaarman.mockitokotlin2.argThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import smarthome.raspberry.authentication.api.flow.SignInFlowLauncher
import smarthome.raspberry.home.api.domain.CreateHomeUseCase
import smarthome.raspberry.home.api.domain.HomeStateMachine
import smarthome.raspberry.home.api.domain.eventbus.events.*
import smarthome.raspberry.home.data.EventBusRepository
import smarthome.raspberry.util.testRxSchedulers

class HomeStateMachineImplTest {
    private lateinit var machine: HomeStateMachine
    private lateinit var eventsRepo: EventBusRepository
    private lateinit var signInFlowLauncher: SignInFlowLauncher
    private lateinit var createHomeUseCase: CreateHomeUseCase
    private lateinit var events: PublishSubject<Event>
    
    @Before
    fun setUp() {
        testRxSchedulers()
        eventsRepo = mock {
            on { getEvents() }.then { events }
        }
        signInFlowLauncher = mock { }
        createHomeUseCase = mock { }
        events = PublishSubject.create()
        machine = HomeStateMachineImpl(eventsRepo, signInFlowLauncher, createHomeUseCase)
        machine.launch()
    }
    
    @Test
    fun `start with putting home to paused state`() {
        verify(eventsRepo).addEvent(argThat { this is Paused })
    }
    
    @Test
    fun `start with need for user`() {
        verify(eventsRepo).addEvent(argThat { this is NeedUser })
    }
    
    @Test
    fun `when has user express need for home id`() {
        machine.registerEvent(HasUser())
        verify(eventsRepo).addEvent(argThat { this is NeedHomeIdentifier })
    }
    
    @Test
    fun `when has user and home id put home in resumed state`() {
        machine.registerEvent(HasUser())
        machine.registerEvent(HasHomeIdentifier())
        verify(eventsRepo).addEvent(argThat { this is Resumed })
    }
    
    @Test
    fun `when need user sign in flow is launched`() {
        events.onNext(NeedUser())
        verify(signInFlowLauncher).launch()
    }
    
    @Test
    fun `when need home identifier create home use case is launched`() {
        events.onNext(NeedHomeIdentifier())
        runBlocking { verify(createHomeUseCase).execute() }
    }
}
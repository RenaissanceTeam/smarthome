package smarthome.raspberry.authentication.domain

import com.nhaarman.mockitokotlin2.argThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Test
import smarthome.raspberry.authentication.api.flow.SignInFlowLauncher
import smarthome.raspberry.authentication.data.AuthRepo
import smarthome.raspberry.home.api.domain.eventbus.ObserveHomeEventsUseCase
import smarthome.raspberry.home.api.domain.eventbus.PublishEventUseCase
import smarthome.raspberry.home.api.domain.eventbus.events.Event
import smarthome.raspberry.home.api.domain.eventbus.events.HasUser
import smarthome.raspberry.home.api.domain.eventbus.events.NeedUser

class AuthenticationStateResolverTest {
    
    private lateinit var eventsUC: ObserveHomeEventsUseCase
    private lateinit var publishEventUseCase: PublishEventUseCase
    private lateinit var events: PublishSubject<Event>
    private lateinit var repo: AuthRepo
    private lateinit var signInFlowLauncher: SignInFlowLauncher
    private lateinit var stateResolver: AuthenticationStateResolver
    
    @Before
    fun setUp() {
        repo = mock {}
        publishEventUseCase = mock {}
        signInFlowLauncher = mock {}
        eventsUC = mock {
            on { execute() }.then { events }
        }
        events = PublishSubject.create()
        stateResolver = AuthenticationStateResolver(repo, publishEventUseCase, signInFlowLauncher, eventsUC)
    }
    
    @Test
    fun `when need user event and user exists should publish has user event`() {
        whenever(repo.hasUser()).then { true }
        events.onNext(NeedUser())
        
        verify(publishEventUseCase).execute(argThat { this is HasUser })
    }
    
    @Test
    fun `when need user and no user should launch sign in flow`() {
        whenever(repo.hasUser()).then { false }
        events.onNext(NeedUser())
    
        verify(signInFlowLauncher).launch()
    }
}
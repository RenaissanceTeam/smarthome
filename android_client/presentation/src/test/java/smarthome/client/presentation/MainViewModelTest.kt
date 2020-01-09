package smarthome.client.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import smarthome.client.domain.api.homeserver.usecases.GetActiveHomeServerUseCase
import smarthome.client.domain.api.usecase.AuthenticationUseCase
import smarthome.client.presentation.util.NavigationEvent
import smarthome.client.util.DataStatus

class MainViewModelTest {
    private lateinit var authUseCase: AuthenticationUseCase
    private lateinit var getActiveHomeServerUseCase: GetActiveHomeServerUseCase
    private lateinit var viewModel: MainViewModel
    private lateinit var authStatus: BehaviorSubject<Boolean>
    private lateinit var loginObserver: Observer<NavigationEvent>
    private lateinit var homeServerObserver: Observer<NavigationEvent>
    
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    
    @Before
    fun setUp() {
        authStatus = BehaviorSubject.create()
        authUseCase = mock {
            on { getAuthenticationStatus() }.then { authStatus }
        }
        getActiveHomeServerUseCase = mock { }
        startKoin {
            modules(module {
                single { authUseCase }
                single { getActiveHomeServerUseCase }
            })
        }
        viewModel = MainViewModel()
        
        loginObserver = mock {}
        homeServerObserver = mock {}
        viewModel.openLogin.observeForever(loginObserver)
        viewModel.openHomeServerSetup.observeForever(homeServerObserver)
    }
    
    @After
    fun tearDown() {
        stopKoin()
    }
//    @Test
//    fun `when user not logged in and there is no home server should not update auth live data`() {
//        whenever(getHomeServerUseCase.execute()).then { throw NoHomeServerException() }
//        viewModel.onCreate()
//
//        assertThat {viewModel.}
//    }
    
    @Test
    fun `when not authenticated should update openLogin livedata`() {
        viewModel.onCreate()
        authStatus.onNext(false)
        verify(loginObserver).onChanged(any())
    }
    
    @Test
    fun `when not authenticated and no home server set should not openLogin, but should openHomeServer`() {
        whenever(getActiveHomeServerUseCase.execute()).then {
            Observable.just(DataStatus.from(null))
        }
        viewModel.onCreate()
        
        authStatus.onNext(false)
        verifyZeroInteractions(loginObserver)
        verify(homeServerObserver).onChanged(any())
    }
}
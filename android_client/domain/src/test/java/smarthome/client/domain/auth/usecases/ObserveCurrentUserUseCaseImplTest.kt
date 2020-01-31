package smarthome.client.domain.auth.usecases

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import smarthome.client.data.api.auth.UserRepository
import smarthome.client.domain.api.auth.usecases.ObserveCurrentUserUseCase
import smarthome.client.domain.api.homeserver.usecases.ObserveActiveHomeServerUseCase
import smarthome.client.entity.HomeServer
import smarthome.client.entity.User
import smarthome.client.util.Data
import smarthome.client.util.EmptyStatus

class ObserveCurrentUserUseCaseImplTest {
    private lateinit var useCase: ObserveCurrentUserUseCase
    private lateinit var userRepo: UserRepository
    private lateinit var observeActiveHomeServerUseCase: ObserveActiveHomeServerUseCase
    
    @Before
    fun setUp() {
        userRepo = mock {}
        observeActiveHomeServerUseCase = mock {}
        useCase = ObserveCurrentUserUseCaseImpl(userRepo, observeActiveHomeServerUseCase)
    }
    
    @Test
    fun `when repo returns empty should emit empty`() {
        whenever(observeActiveHomeServerUseCase.execute())
            .then { Observable.just(Data<HomeServer>(mock {})) }
        
        whenever(userRepo.get()).then { Observable.just(listOf<User>()) }
        
        useCase.execute().test().assertValue { it is EmptyStatus }
    }
    
    @Test
    fun `when empty active home server should emit empty`() {
        whenever(observeActiveHomeServerUseCase.execute()).then { Observable.just(EmptyStatus<HomeServer>()) }
        whenever(userRepo.get()).then { Observable.just(mock<List<User>>()) }
    
        useCase.execute().test().assertValue { it is EmptyStatus}
    }
    
    @Test
    fun `when repo returns users from different servers should choose from current one`() {
        val activeServerId = 2
        val activeServer = mock<HomeServer> {
            on { active }.then { true }
            on { id }.then { activeServerId }
        }
        val user1 = mock<User> { on { serverId }.then { 1 } }
        val user2 = mock<User> { on { serverId }.then { activeServerId } }
        
        val users = listOf(user1, user2)
        whenever(observeActiveHomeServerUseCase.execute()).then { Observable.just(Data(activeServer)) }
        whenever(userRepo.get()).then { Observable.just(users) }
        
        useCase.execute().test().assertValue { it is Data && it.data == user2 }
    }
}
package smarthome.client.domain.homeserver.usecases

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import smarthome.client.data.api.homeserver.HomeServerRepo
import smarthome.client.domain.api.homeserver.usecases.ObserveActiveHomeServerUseCase
import smarthome.client.entity.HomeServer
import smarthome.client.util.Data
import smarthome.client.util.DataStatus
import smarthome.client.util.EmptyStatus


class ObserveActiveHomeServerUseCaseImplTest {
    private lateinit var useCaseActive: ObserveActiveHomeServerUseCase
    private lateinit var repo: HomeServerRepo
    
    
    @Before
    fun setUp() {
        repo = mock {}
        useCaseActive = ObserveActiveHomeServerUseCaseImpl(repo)
    }
    
    @Test
    fun `when empty list emited should emit empty value`() {
        whenever(repo.get()).then { Observable.just(listOf<HomeServer>()) }
        useCaseActive.execute().test().assertValue { it is EmptyStatus }
    }
    
    @Test
    fun `when list with many servers should map to active server only`() {
        val activeServer = mock<HomeServer> {
            on { active }.then { true }
        }
        val notActiveServer = mock<HomeServer> {
            on { active }.then { false }
        }
        val servers = listOf(notActiveServer, activeServer, notActiveServer, notActiveServer)
        whenever(repo.get()).then { Observable.just(servers) }
        
        useCaseActive.execute().test().assertValue { it is Data && it.data == activeServer }
    }
}
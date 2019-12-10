package smarthome.raspberry.home.domain

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Test
import smarthome.raspberry.home.api.domain.ObserveHomeIdUseCase
import smarthome.raspberry.home.data.HomeRepository
import smarthome.raspberry.home.domain.ObserveHomeIdUseCaseImpl.Companion.HOME_ID
import smarthome.raspberry.util.persistence.StorageHelper
import smarthome.raspberry.util.persistence.get
import smarthome.raspberry.util.persistence.observe

class ObserveHomeIdUseCaseImplTest {
    private lateinit var repo: HomeRepository
    private lateinit var useCase: ObserveHomeIdUseCase
    
    @Before
    fun setUp() {
        repo = mock { }
        useCase = ObserveHomeIdUseCaseImpl(repo)
    }
    
    @Test
    fun `should use repository as the source of home id`() {
        useCase.execute()
        verify(repo).getHomeId()
    }
}
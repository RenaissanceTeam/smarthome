package smarthome.raspberry.home.domain

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test
import smarthome.raspberry.home.api.domain.ObserveHomeIdUseCase
import smarthome.raspberry.home.data.storage.LocalStorage

class ObserveHomeIdUseCaseImplTest {
    private lateinit var localStorage: LocalStorage
    private lateinit var useCase: ObserveHomeIdUseCase
    
    @Before
    fun setUp() {
        localStorage = mock { }
        useCase = ObserveHomeIdUseCaseImpl(localStorage)
    }
    
    @Test
    fun `should use localStorage as the source of home id`() {
        useCase.execute()
        verify(localStorage).getHomeId()
    }
}
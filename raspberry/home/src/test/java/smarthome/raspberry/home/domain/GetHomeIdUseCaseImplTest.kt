package smarthome.raspberry.home.domain

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Test
import smarthome.raspberry.home.api.domain.GetHomeIdUseCase
import smarthome.raspberry.home.domain.GetHomeIdUseCaseImpl.Companion.HOME_ID
import smarthome.raspberry.util.persistence.StorageHelper
import smarthome.raspberry.util.persistence.get

class GetHomeIdUseCaseImplTest {
    private lateinit var prefs: StorageHelper
    private lateinit var useCase: GetHomeIdUseCase
    
    @Before
    fun setUp() {
        prefs = mock { }
        useCase = GetHomeIdUseCaseImpl(prefs)
    }
    
    @Test
    fun `when there is no saved home id should emit empty string`() {
        whenever(prefs.get<String>(HOME_ID)).then { throw IllegalArgumentException() }
        val result = useCase.execute().test()
        
        result.assertValue { it.isEmpty() }
    }
    
    @Test
    fun `when there is saved home id should return it`() {
        val homeId = "home id"
        whenever(prefs.get<String>(HOME_ID)).then { homeId }
    
        val result = useCase.execute().test()
    
        result.assertValue { it == homeId }
    }
}
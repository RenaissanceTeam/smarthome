package smarthome.raspberry.home.domain

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Test
import smarthome.raspberry.home.api.domain.GetHomeIdUseCase
import smarthome.raspberry.home.domain.GetHomeIdUseCaseImpl.Companion.EMPTY_HOME_ID
import smarthome.raspberry.home.domain.GetHomeIdUseCaseImpl.Companion.HOME_ID
import smarthome.raspberry.util.persistence.SharedPreferencesHelper

class GetHomeIdUseCaseImplTest {
    private lateinit var prefs: SharedPreferencesHelper
    private lateinit var useCase: GetHomeIdUseCase
    
    @Before
    fun setUp() {
        prefs = mock { }
        useCase = GetHomeIdUseCaseImpl(prefs)
    }
    
    @Test
    fun `when there is no saved home id should emit empty string`() {
        whenever(prefs.getString(HOME_ID, EMPTY_HOME_ID)).then { EMPTY_HOME_ID }
        val result = useCase.execute().test()
        
        result.assertValue { it.isEmpty() }
    }
    
    @Test
    fun `when there is saved home id should return it`() {
        val homeId = "home id"
        whenever(prefs.getString(HOME_ID, EMPTY_HOME_ID)).then { homeId }
        
        val result = useCase.execute().test()
    
        result.assertValue { it == homeId }
    }
}
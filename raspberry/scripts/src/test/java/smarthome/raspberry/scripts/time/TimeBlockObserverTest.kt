package smarthome.raspberry.scripts.time

import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import smarthome.raspberry.scripts.api.domain.usecase.GetBlockByIdUseCase
import smarthome.raspberry.scripts.api.domain.usecase.GetBlockConditionDependenciesUseCase
import java.util.*

class TimeBlockObserverTest {
    private lateinit var getBlockByIdUseCase: GetBlockByIdUseCase
    private lateinit var getBlockConditionDependenciesUseCase: GetBlockConditionDependenciesUseCase
    private lateinit var observer: TimeBlockObserver

    @Before
    fun setUp() {
        getBlockByIdUseCase = mock {}
        getBlockConditionDependenciesUseCase = mock {}

        observer = TimeBlockObserver(getBlockConditionDependenciesUseCase, getBlockByIdUseCase)
    }

    @Test
    fun `satrt of owewek`() {
        assertEquals(
                Calendar.getInstance().apply { set(2020, Calendar.MAY, 11, 0, 0, 0) }.timeInMillis,
                observer.getStartOfWeek()
        )
    }
}
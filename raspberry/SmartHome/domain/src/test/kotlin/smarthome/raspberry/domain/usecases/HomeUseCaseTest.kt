package smarthome.raspberry.domain.usecases

import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import smarthome.raspberry.domain.HomeRepository


@RunWith(JUnit4::class)
class HomeUseCaseTest {

    @Mock
    private lateinit var repo: HomeRepository
    private val homeUseCase by lazy { HomeUseCase(repo) }

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun noIdsSaved_GenerateUniqueHomeId_AddGeneratedIdToRepo() {
        runBlocking {
            whenever(repo.isHomeIdUnique(ArgumentMatchers.anyString())).thenReturn(true)
            homeUseCase.generateUniqueHomeId()

            verify(repo, times(1)).isHomeIdUnique(anyString())
        }
    }

    @Test
    fun hasSomeIdsSaved_GenerateUniqueHomeId_ManyAttemptsOfGeneratingId() {
        runBlocking {
            val retryCount = 4
            var currentRetry = 0
            whenever(repo.isHomeIdUnique(ArgumentMatchers.anyString())).thenAnswer {
                return@thenAnswer ++currentRetry == retryCount
            }

            homeUseCase.generateUniqueHomeId()
            verify(repo, times(retryCount)).isHomeIdUnique(ArgumentMatchers.anyString())
        }
    }

}
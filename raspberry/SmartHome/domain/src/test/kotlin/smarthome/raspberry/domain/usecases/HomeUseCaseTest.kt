package smarthome.raspberry.domain.usecases

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import smarthome.raspberry.domain.HomeRepository
import smarthome.raspberry.domain.models.HomeInfo


@RunWith(JUnit4::class)
class HomeUseCaseTest {

    private val repo: HomeRepository = mock()
    private val homeUseCase = HomeUseCase(repo)
    private val homeStates = PublishSubject.create<HomeInfo>()

    @Before
    fun setUp() {
        whenever(repo.getHomeInfo()).then { homeStates }
        homeUseCase.launchStateMachine(Schedulers.trampoline())
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

    @Test
    fun hasAuthenticatedUser_noHomeId_shouldCreateHomeId() {
        runBlocking {
            whenever(repo.isHomeIdUnique(ArgumentMatchers.anyString())).then { true }

            homeStates.onNext(HomeInfo("", ""))
            homeStates.onNext(HomeInfo("user_id", ""))
            verify(repo).saveHome(ArgumentMatchers.anyString())
        }
    }


}
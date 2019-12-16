package smarthome.raspberry.home.domain

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import smarthome.raspberry.entity.HomeInfo
import smarthome.raspberry.home.api.domain.GenerateUniqueHomeIdUseCase
import smarthome.raspberry.home.api.domain.GetHomeInfoUseCase
import smarthome.raspberry.home.api.domain.LaunchUseCase
import smarthome.raspberry.home.data.HomeRepository

class LaunchUseCaseImplTest {
    
    private lateinit var launchUseCase: LaunchUseCase
    private lateinit var generateUniqueHomeIdUseCase: GenerateUniqueHomeIdUseCase
    private lateinit var repository: HomeRepository
    private lateinit var getHomeInfoUseCase: GetHomeInfoUseCase
    
    @Before
    fun setUp() {
        generateUniqueHomeIdUseCase = mock {}
        repository = mock {}
        getHomeInfoUseCase = mock {}
    
        launchUseCase =
            LaunchUseCaseImpl(generateUniqueHomeIdUseCase, repository, getHomeInfoUseCase)
    }
    
    @Test
    fun `when user authenticated without home id should generete id and save to home repo`() {
        val homeId = "id"
        whenever(getHomeInfoUseCase.execute()).then { Observable.just(HomeInfo("uid", "")) }
        
        
        runBlocking {
            whenever(generateUniqueHomeIdUseCase.execute()).then { homeId }
            
            launchUseCase.execute()
    
            verify(generateUniqueHomeIdUseCase).execute()
            verify(repository).saveHome(homeId)
        }
    }
}
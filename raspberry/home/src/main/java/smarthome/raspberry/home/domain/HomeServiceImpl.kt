package smarthome.raspberry.home.domain

import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.runBlocking
import smarthome.raspberry.home.api.domain.GetHomeInfoUseCase
import smarthome.raspberry.home.api.domain.HomeService
import smarthome.raspberry.home.data.HomeRepository
import java.util.*
import kotlin.random.Random

class HomeServiceImpl(
        private val repository: HomeRepository,
        private val getHomeInfoUseCase: GetHomeInfoUseCase
) : HomeService {
    private val HOME_ID_PREFIX = "home_id"
    private var stateDisposable: Disposable? = null


    override suspend fun generateUniqueHomeId(): String {
        var homeId: String
        do {
            homeId = generateHomeId()
            val isUnique = repository.isHomeIdUnique(homeId)
        } while (!isUnique)
        return homeId
    }

    private fun generateHomeId(): String {
        val currentTime = Date().time
        val randomPart = Random.nextInt(0, 9999).toString()

        return "$HOME_ID_PREFIX$currentTime$randomPart"
    }

    override fun launchStateMachine(scheduler: Scheduler) {
        stateDisposable = getHomeInfoUseCase.execute()
                .observeOn(scheduler)
                .subscribe(
                        {
                            runBlocking {
                                if (hasUser(it) && !hasHomeId(it)) {
                                    createHome()
                                }
                            }
                        },
                        {
                            it.printStackTrace()

                        }
                )
    }

    private fun hasUser(it: smarthome.raspberry.entity.HomeInfo) = it.userId.isNotEmpty()

    private fun hasHomeId(info: smarthome.raspberry.entity.HomeInfo) = info.homeId.isNotEmpty()

    private suspend fun createHome() {
        val homeId = generateUniqueHomeId()
        repository.saveHome(homeId)
    }
}
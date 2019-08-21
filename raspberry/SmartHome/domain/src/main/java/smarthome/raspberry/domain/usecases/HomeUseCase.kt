package smarthome.raspberry.domain.usecases

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.runBlocking
import smarthome.raspberry.domain.HomeRepository
import smarthome.raspberry.domain.Log
import smarthome.raspberry.domain.log
import smarthome.raspberry.domain.models.HomeInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

class HomeUseCase(private val repository: HomeRepository) {
    private val HOME_ID_PREFIX = "home_id"
    private var stateDisposable: Disposable? = null

    suspend fun generateUniqueHomeId(): String {
        var homeId: String
        do {
            homeId = generateHomeId()
            val isUnique = repository.isHomeIdUnique(homeId)
        } while (!isUnique)
        return homeId
    }

    private fun generateHomeId(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
        val currentTime = LocalDateTime.now().format(formatter)

        val randomPart = Random.nextInt(0, 9999).toString()

        return "$HOME_ID_PREFIX$currentTime$randomPart"
    }

    fun getHomeInfo(): Observable<HomeInfo> {
        return repository.getHomeInfo()
    }

    fun launchStateMachine(scheduler: Scheduler = Schedulers.io()) {
        stateDisposable = repository.getHomeInfo()
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

    private fun hasUser(it: HomeInfo) = it.userId.isNotEmpty()

    private fun hasHomeId(info: HomeInfo) = info.homeId.isNotEmpty()

    private suspend fun createHome() {
        val homeId = generateUniqueHomeId()
        repository.saveHome(homeId)
    }
}
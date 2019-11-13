package smarthome.raspberry.home.api.domain

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

interface HomeService {
    suspend fun generateUniqueHomeId(): String
    fun launchStateMachine(scheduler: Scheduler = Schedulers.io())
}
package smarthome.raspberry.home_api.domain

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import smarthome.raspberry.entity.HomeInfo

interface HomeService {
    suspend fun generateUniqueHomeId(): String
    fun getHomeInfo(): Observable<HomeInfo>
    fun launchStateMachine(scheduler: Scheduler = Schedulers.io())
}
package smarthome.raspberry.home.domain

import android.annotation.SuppressLint
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.runBlocking
import smarthome.raspberry.home.api.domain.GenerateUniqueHomeIdUseCase
import smarthome.raspberry.home.api.domain.GetHomeInfoUseCase
import smarthome.raspberry.home.api.domain.LaunchUseCase
import smarthome.raspberry.home.data.HomeRepository

class LaunchUseCaseImpl(
        private val generateUniqueHomeIdUseCase: GenerateUniqueHomeIdUseCase,
        private val repository: HomeRepository,
        private val getHomeInfoUseCase: GetHomeInfoUseCase
) : LaunchUseCase {

    @SuppressLint("CheckResult")
    override fun execute() {
        getHomeInfoUseCase.execute()
                .observeOn(Schedulers.io())
                .subscribeBy(
                        onNext = {
                            runBlocking {
                                if (hasUser(it) && !hasHomeId(it)) {
                                    createHome()
                                }
                            }
                        },
                        onError = {}
                )

        // todo listen for data changes - user input
        // todo listen for devices - devices input

    }

    private fun hasUser(it: smarthome.raspberry.entity.HomeInfo) = it.userId.isNotEmpty()
    private fun hasHomeId(info: smarthome.raspberry.entity.HomeInfo) = info.homeId.isNotEmpty()

    private suspend fun createHome() {
        val homeId = generateUniqueHomeIdUseCase.execute()
        repository.saveHome(homeId)
    }
}
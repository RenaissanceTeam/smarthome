package smarthome.raspberry.home.domain

import android.annotation.SuppressLint
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.runBlocking
import smarthome.raspberry.entity.HomeInfo
import smarthome.raspberry.home.api.domain.GenerateUniqueHomeIdUseCase
import smarthome.raspberry.home.api.domain.GetHomeInfoUseCase
import smarthome.raspberry.home.api.domain.HomeStateMachine
import smarthome.raspberry.home.api.domain.LaunchUseCase
import smarthome.raspberry.home.data.HomeRepository

class LaunchUseCaseImpl(
    private val homeStateMachine: HomeStateMachine
) : LaunchUseCase {

    @SuppressLint("CheckResult")
    override fun execute() {
        homeStateMachine.launch()
    }
}
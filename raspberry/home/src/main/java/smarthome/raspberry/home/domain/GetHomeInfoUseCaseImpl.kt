package smarthome.raspberry.home.domain

import io.reactivex.Observable
import smarthome.raspberry.entity.HomeInfo
import smarthome.raspberry.home.api.domain.GetHomeInfoUseCase
import smarthome.raspberry.home.data.HomeRepository

class GetHomeInfoUseCaseImpl(
        private val repository: HomeRepository
) : GetHomeInfoUseCase {
    override fun execute(): Observable<HomeInfo> {
        TODO()
    }
}
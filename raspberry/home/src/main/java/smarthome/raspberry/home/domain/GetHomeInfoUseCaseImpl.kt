package smarthome.raspberry.home.domain

import io.reactivex.Observable
import smarthome.raspberry.authentication.api.domain.GetUserIdUseCase
import smarthome.raspberry.entity.HomeInfo
import smarthome.raspberry.home.api.domain.GetHomeIdUseCase
import smarthome.raspberry.home.api.domain.GetHomeInfoUseCase
import smarthome.raspberry.home.data.HomeRepository

class GetHomeInfoUseCaseImpl(
        private val repository: HomeRepository,
        private val getHomeIdUseCase: GetHomeIdUseCase,
        private val getUserIdUseCase: GetUserIdUseCase
) : GetHomeInfoUseCase {
    override fun execute(): Observable<HomeInfo> {
        return repository.getHomeInfo(getUserIdUseCase.execute(),
                               getHomeIdUseCase.execute())
    }
}
package smarthome.raspberry.home.api.domain

import io.reactivex.Observable
import smarthome.raspberry.entity.HomeInfo

interface GetHomeInfoUseCase {
    fun execute(): Observable<HomeInfo>
}
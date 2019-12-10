package smarthome.raspberry.home.api.domain

import io.reactivex.Observable

interface ObserveHomeIdUseCase {
    fun execute(): Observable<String>
}
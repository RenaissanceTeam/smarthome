package smarthome.raspberry.controllers.api.domain

import io.reactivex.Observable
import java.util.*

interface ObserveControllerStatesUseCase {
    fun execute(id: Long): Observable<Optional<String>>
}
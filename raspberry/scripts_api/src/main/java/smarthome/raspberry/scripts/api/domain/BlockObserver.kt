package smarthome.raspberry.scripts.api.domain

import io.reactivex.Observable
import smarthome.raspberry.entity.script.Block
import java.util.*

interface BlockObserver<T: ConditionState> {
    fun execute(blockId: String): Observable<Optional<T>>
}
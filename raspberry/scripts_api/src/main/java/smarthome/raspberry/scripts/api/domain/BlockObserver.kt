package smarthome.raspberry.scripts.api.domain

import io.reactivex.Observable
import smarthome.raspberry.entity.script.Block
import java.util.*

interface BlockObserver {
    fun execute(blockId: String): Observable<Optional<Block>>
}
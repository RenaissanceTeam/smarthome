package smarthome.raspberry.scripts.time

import io.reactivex.Observable
import org.springframework.stereotype.Component
import smarthome.raspberry.entity.script.Block
import smarthome.raspberry.scripts.api.domain.BlockObserver
import java.util.*

@Component
class TimeBlockObserver(

) : BlockObserver {
    override fun execute(blockId: String): Observable<Optional<Block>> {
        return Observable.empty()
    }
}
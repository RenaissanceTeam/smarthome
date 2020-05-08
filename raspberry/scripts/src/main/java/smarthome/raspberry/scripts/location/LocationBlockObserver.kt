package smarthome.raspberry.scripts.location

import io.reactivex.Observable
import org.springframework.stereotype.Component
import smarthome.raspberry.entity.script.Block
import smarthome.raspberry.scripts.api.domain.BlockObserver
import java.util.*

@Component
class LocationBlockObserver : BlockObserver {
    override fun execute(blockId: String): Observable<Optional<Block>> {
        return Observable.empty()
    }
}
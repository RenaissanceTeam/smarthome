package smarthome.raspberry.scripts.location

import io.reactivex.Observable
import org.springframework.stereotype.Component
import smarthome.raspberry.scripts.api.domain.BlockObserver
import smarthome.raspberry.scripts.api.location.LocationConditionState
import java.util.*

@Component
class LocationBlockObserver : BlockObserver<LocationConditionState> {
    override fun execute(blockId: String): Observable<Optional<LocationConditionState>> {
        return Observable.empty()
    }
}
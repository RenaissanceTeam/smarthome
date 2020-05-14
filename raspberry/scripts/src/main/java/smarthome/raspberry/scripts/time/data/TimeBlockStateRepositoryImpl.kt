package smarthome.raspberry.scripts.time.data

import io.reactivex.subjects.BehaviorSubject
import org.joda.time.LocalTime
import org.springframework.stereotype.Component
import smarthome.raspberry.entity.script.Block
import smarthome.raspberry.scripts.api.time.data.TimeBlockStateRepository
import java.util.*

@Component
class TimeBlockStateRepositoryImpl : TimeBlockStateRepository {
    private val observables = mutableMapOf<String, BehaviorSubject<Optional<Block>>>()
    private val timers = mutableMapOf<String, Timer>()

    private fun getOrCreateObservable(blockId: String): BehaviorSubject<Optional<Block>> {
        return observables[blockId] ?: BehaviorSubject.createDefault<Optional<Block>>(Optional.empty())
                .also { observables[blockId] = it }
    }

    fun schedule(blockId: String, time: LocalTime) {


//        getOrCreateObservable(blockId).
    }

//
//    override fun scheduleNext(id: String, time: LocalTime): Observable<Long> {
//        val timer = createNewTimer(id)
//        val observable = PublishSubject.create<Long>()
//        val time = time.toDateTimeToday().millis
//        timer.schedule(Date(time)) {
//            observable.onNext(time)
//        }
//    }


    private fun createNewTimer(id: String): Timer {
        timers[id]?.apply { cancel() }?.also { timers.remove(id) }
        return Timer(id)
    }

    private fun getOrCreateTimer(id: String): Timer {
        return timers[id] ?: Timer(id).also { timers[id] = it }
    }


}

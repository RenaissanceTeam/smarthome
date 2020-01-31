package smarthome.library.common.util.delegates

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class BehaviourSubjectFromObservable<R, T>(
        private val observableFactory: () -> Observable<T>
) : ReadOnlyProperty<R, BehaviorSubject<T>> {
    
    override fun getValue(thisRef: R, property: KProperty<*>): BehaviorSubject<T> {
        return BehaviorSubject.create<T>().apply {
            observableFactory.invoke().subscribe { onNext(it) }
        }
    }
}
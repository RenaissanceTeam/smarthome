package smarthome.raspberry.util.persistence.preferences

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import kotlin.reflect.KClass

class ObservablePreference<T : Any>(private val from: Preference<T>) : Preference<T> {
    private val subject = PublishSubject.create<T>()
    
    override val ofType: KClass<T> get() = from.ofType
    
    override fun get(): T {
        return from.get()
    }
    
    override fun setDefault(value: T) {
        from.setDefault(value)
    }
    
    override suspend fun set(value: T) {
        from.set(value)
        subject.onNext(value)
    }
    
    fun observe(): Observable<T> = subject
}
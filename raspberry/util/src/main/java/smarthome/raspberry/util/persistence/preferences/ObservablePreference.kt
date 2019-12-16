package smarthome.raspberry.util.persistence.preferences

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import smarthome.raspberry.util.persistence.NoStoredPreference
import kotlin.reflect.KClass

class ObservablePreference<T : Any>(private val from: Preference<T>) : Preference<T> {
    private val subject = BehaviorSubject.create<T>()
    
    override val key: String get() = from.key
    
    override var default: T?
        get() = from.default
        set(value) {
            value ?: return
            
            from.default = value
            if (subject.value == null) subject.onNext(value)
        }
    
    init {
        try {
            subject.onNext(from.get())
        } catch (ignored: NoStoredPreference) {
        
        }
    }
    
    override val ofType: KClass<T> get() = from.ofType
    
    override fun get(): T {
        return from.get()
    }
    
    override suspend fun set(value: T) {
        from.set(value)
        subject.onNext(value)
    }
    
    fun observe(): Observable<T> = subject
}
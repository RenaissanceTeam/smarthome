package smarthome.raspberry.util.persistence.preferences

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class ObservablePreference<T>(key: String) {
    private val subject = BehaviorSubject.create<T>()
    
    fun observe(): Observable<T> {
        return subject
    }
    
    fun getValue(default: T): T = subject.value ?: default
}
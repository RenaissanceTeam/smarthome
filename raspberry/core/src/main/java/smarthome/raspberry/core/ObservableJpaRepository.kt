package smarthome.raspberry.core

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.JpaEntityInformation
import org.springframework.data.jpa.repository.support.SimpleJpaRepository
import org.springframework.data.repository.findByIdOrNull
import java.util.*
import javax.persistence.EntityManager

abstract class ObservableJpaRepository<T, ID>(
        protected val jpaRepository: JpaRepository<T, ID>
) {
    protected val observables = mutableMapOf<ID, BehaviorSubject<Optional<T>>>()

    abstract fun getEntityId(entity: T): ID

    fun observeById(id: ID): Observable<Optional<T>> {
        return observables[id]
                ?: BehaviorSubject.createDefault(
                        jpaRepository.findByIdOrNull(id).toOptional()
                ).also { observables[id] = it }
    }

    fun save(entity: T): T {
        return jpaRepository.save(entity).also {
            observables[getEntityId(entity)]?.onNext(it.toOptional())
        }
    }
}
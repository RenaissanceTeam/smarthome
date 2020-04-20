package smarthome.raspberry.core

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import org.springframework.data.jpa.repository.support.JpaEntityInformation
import org.springframework.data.jpa.repository.support.SimpleJpaRepository
import org.springframework.data.repository.findByIdOrNull
import java.util.*
import javax.persistence.EntityManager

class ObservableJpaRepositoryImpl<T, ID>(
        private val entityInformation: JpaEntityInformation<T, ID>,
        entityManager: EntityManager
) : SimpleJpaRepository<T, ID>(entityInformation, entityManager), ObservableJpaRepository<T, ID> {
    private val observables = mutableMapOf<ID, BehaviorSubject<Optional<T>>>()

    override fun observeById(id: ID): Observable<Optional<T>> {
        return observables[id] ?: BehaviorSubject.createDefault(findByIdOrNull(id).toOptional()).also { observables[id] = it }
    }

    override fun <S : T> save(entity: S): S {
        return super.save(entity).also {
            val id = entityInformation.getId(it) ?: return@also

            observables[id]?.onNext(it.toOptional())
        }
    }
}
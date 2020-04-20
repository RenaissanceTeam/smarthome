package smarthome.raspberry.core

import io.reactivex.Observable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean
import java.util.*

@NoRepositoryBean
interface ObservableJpaRepository<T, ID> : JpaRepository<T, ID> {
    fun observeById(id: ID): Observable<Optional<T>>
}


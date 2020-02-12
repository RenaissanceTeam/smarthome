package smarthome.client.domain.api.scripts.usecases.dependency

import io.reactivex.Observable
import smarthome.client.entity.script.dependency.DependencyDetails
import smarthome.client.entity.script.dependency.DependencyId

interface ObserveSetupDependencyUseCase {
    fun execute(scriptId: Long, dependencyId: DependencyId): Observable<DependencyDetails>
}
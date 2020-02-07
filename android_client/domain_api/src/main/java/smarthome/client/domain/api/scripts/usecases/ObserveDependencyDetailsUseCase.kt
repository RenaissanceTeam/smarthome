package smarthome.client.domain.api.scripts.usecases

import io.reactivex.Observable
import smarthome.client.entity.script.dependency.DependencyDetails
import smarthome.client.entity.script.dependency.DependencyId
import smarthome.client.util.DataStatus

interface ObserveDependencyDetailsUseCase {
    fun execute(scriptId: Long, dependencyId: DependencyId): Observable<DataStatus<DependencyDetails>>
}
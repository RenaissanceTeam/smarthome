package smarthome.client.domain.scripts.usecases

import io.reactivex.Observable
import smarthome.client.data.api.scripts.DependencyRepo
import smarthome.client.domain.api.scripts.usecases.ObserveDependencyDetailsUseCase
import smarthome.client.entity.script.dependency.DependencyDetails
import smarthome.client.entity.script.dependency.DependencyId
import smarthome.client.util.DataStatus

class ObserveDependencyDetailsUseCaseImpl(
    private val repo: DependencyRepo
) : ObserveDependencyDetailsUseCase {
    override fun execute(scriptId: Long, dependencyId: DependencyId): Observable<DataStatus<DependencyDetails>> {
        return repo.observeDetails(scriptId, dependencyId)
    }
}
package smarthome.client.domain.scripts.usecases

import io.reactivex.Observable
import smarthome.client.data.api.scripts.ScriptGraphRepo
import smarthome.client.domain.api.scripts.usecases.ObserveDependenciesUseCase
import smarthome.client.entity.script.Dependency

class ObserveDependenciesUseCaseImpl(
    private val repo: ScriptGraphRepo
) : ObserveDependenciesUseCase {
    override fun execute(scriptId: Long): Observable<List<Dependency>> {
        return repo.observeDependencies(scriptId)
    }
}
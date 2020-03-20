package smarthome.client.domain.scripts.usecases.setup

import io.reactivex.Observable
import smarthome.client.data.api.scripts.SetupScriptRepo
import smarthome.client.domain.api.scripts.usecases.setup.ObserveDependenciesUseCase
import smarthome.client.entity.script.dependency.Dependency

class ObserveDependenciesUseCaseImpl(
    private val repo: SetupScriptRepo
) : ObserveDependenciesUseCase {
    override fun execute(): Observable<List<Dependency>> {
        return repo.observeDependencies()
    }
}
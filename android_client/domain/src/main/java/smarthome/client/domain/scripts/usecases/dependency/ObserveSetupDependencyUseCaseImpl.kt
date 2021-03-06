package smarthome.client.domain.scripts.usecases.dependency

import io.reactivex.Observable
import smarthome.client.data.api.scripts.SetupDependencyRepo
import smarthome.client.domain.api.scripts.usecases.dependency.ObserveSetupDependencyUseCase
import smarthome.client.entity.script.dependency.Dependency

class ObserveSetupDependencyUseCaseImpl(
        private val repo: SetupDependencyRepo
) : ObserveSetupDependencyUseCase {
    override fun execute(): Observable<Dependency> {
        return repo.observe().distinctUntilChanged()
    }
}
package smarthome.client.domain.api.scripts.usecases

import io.reactivex.Observable
import smarthome.client.entity.script.dependency.Dependency

interface ObserveDependenciesUseCase {
    fun execute(scriptId: Long): Observable<List<Dependency>>
}
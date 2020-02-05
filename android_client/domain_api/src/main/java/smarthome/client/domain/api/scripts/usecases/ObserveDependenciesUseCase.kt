package smarthome.client.domain.api.scripts.usecases

import io.reactivex.Observable
import smarthome.client.entity.script.Dependency

interface ObserveDependenciesUseCase {
    fun execute(scriptId: Long): Observable<List<Dependency>>
}
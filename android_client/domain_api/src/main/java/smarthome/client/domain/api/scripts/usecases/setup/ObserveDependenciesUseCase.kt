package smarthome.client.domain.api.scripts.usecases.setup

import io.reactivex.Observable
import smarthome.client.entity.script.dependency.Dependency

interface ObserveDependenciesUseCase {
    fun execute(): Observable<List<Dependency>>
}
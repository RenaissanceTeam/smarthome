package smarthome.client.domain.api.scripts.usecases.dependency

import io.reactivex.Observable
import smarthome.client.entity.script.dependency.Dependency

interface ObserveSetupDependencyUseCase {
    fun execute(): Observable<Dependency>
}
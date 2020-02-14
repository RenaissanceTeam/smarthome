package smarthome.client.domain.api.scripts.usecases.dependency

import io.reactivex.Observable
import smarthome.client.entity.script.dependency.DependencyDetails
import smarthome.client.util.DataStatus

interface ObserveSetupDependencyUseCase {
    fun execute(): Observable<DependencyDetails>
}
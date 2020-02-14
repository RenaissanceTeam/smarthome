package smarthome.client.data.api.scripts

import io.reactivex.Observable
import smarthome.client.entity.script.dependency.DependencyDetails
import smarthome.client.entity.script.dependency.DependencyId
import smarthome.client.util.DataStatus

interface DependencyRepo {
    suspend fun getDetails(scriptId: Long, dependencyId: DependencyId): DependencyDetails
    fun observeDetails(scriptId: Long, dependencyId: DependencyId): Observable<DataStatus<DependencyDetails>>
}
package smarthome.client.data.controllers

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import smarthome.client.data.api.controllers.ControllersRepo
import smarthome.client.data.controllers.dto.StateDto
import smarthome.client.data.retrofit.RetrofitFactory
import smarthome.client.entity.Controller
import smarthome.client.util.*

class ControllersRepoImpl(
    private val retrofitFactory: RetrofitFactory
) : ControllersRepo {
    private val controllers = mutableMapOf<Long, BehaviorSubject<DataStatus<Controller>>>()
    
    override fun observe(id: Long): Observable<DataStatus<Controller>> {
        return getOrCreateSubject(id)
    }
    
    private fun getOrCreateSubject(id: Long): BehaviorSubject<DataStatus<Controller>> {
        return controllers[id]
            ?: BehaviorSubject.createDefault<DataStatus<Controller>>(EmptyStatus())
                .also { controllers[id] = it }
    }
    
    override fun controllerUpdated(controller: Controller) {
        emitData(controller.id, controller)
    }

    override suspend fun updateName(id: Long, name: String) {
        retrofitFactory.createApi(ControllersApi::class.java)
                .updateName(id, name)
    }

    override suspend fun setState(controllerId: Long, state: String): String {
        if (noController(controllerId)) {
            fetch(controllerId)
        }
        
        emitLoading(controllerId)
        return retrofitFactory.createApi(ControllersApi::class.java)
            .runCatching { changeState(controllerId, StateDto(state)) }
            .onSuccess {
                emitData(
                    controllerId,
                    getLastControllerOrThrow(controllerId).copy(state = it.state)
                )
            }
            .onFailure { emitError(controllerId, it) }
            .getOrThrow().state
    }
    
    private fun emitLoading(id: Long) {
        val subject = getOrCreateSubject(id)
        val lastData = getLastData(id)
        
        subject.onNext(LoadingStatus(lastData))
    }
    
    private fun emitData(id: Long, controller: Controller) {
        val subject = getOrCreateSubject(id)
        subject.onNext(Data(controller))
    }
    
    private fun emitError(id: Long, cause: Throwable) {
        val subject = getOrCreateSubject(id)
        val lastData = getLastData(id)
    
        subject.onNext(ErrorStatus(cause, lastData))
    }
    
    private fun noController(id: Long) = getLastData(id) == null
    
    private fun getLastControllerOrThrow(id: Long): Controller {
        return getLastData(id)?.data ?: throw Throwable("No info about controller")
    }
    
    private fun getLastData(id: Long): Data<Controller>? {
        return when (val value = getOrCreateSubject(id).value) {
            is Data -> value
            is ErrorStatus -> value.lastData
            is LoadingStatus -> value.lastData
            else -> null
        }
    }
    
    override suspend fun readState(controllerId: Long): String {
        if (noController(controllerId)) {
            fetch(controllerId)
        }
    
        emitLoading(controllerId)
        return retrofitFactory.createApi(ControllersApi::class.java)
            .runCatching { readState(controllerId) }
            .onSuccess {
                emitData(
                    controllerId,
                    getLastControllerOrThrow(controllerId).copy(state = it.state)
                )
            }
            .onFailure { emitError(controllerId, it) }
            .getOrThrow().state
    }
    
    override fun get(id: Long): Controller? {
        return controllers[id]?.value?.data
    }
    
    override suspend fun fetch(id: Long): Controller {
        emitLoading(id)
        return retrofitFactory.createApi(ControllersApi::class.java)
            .runCatching { getDetails(id) }
            .onSuccess { emitData(id, it) }
            .onFailure { emitError(id, it) }
            .getOrThrow()
    }
}
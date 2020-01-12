package smarthome.client.data.controllers

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.delay
import smarthome.client.data.api.controllers.ControllersRepo
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
    
    override suspend fun setState(controllerId: Long, state: String): String {
        if (noController(controllerId)) {
            get(controllerId)
        }
        
        emitLoading(controllerId)
        retrofitFactory.createApi(ControllersApi::class.java)
            .runCatching { changeState(controllerId, state) }
            .onSuccess {
                val lastController = getLastData(controllerId)?.data
                    ?: throw Throwable("No info about controller when setState")
                emitData(controllerId, lastController.copy(state = it))
            }
            .onFailure { emitError(controllerId, it) }
            .getOrThrow()
        
        return state
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
    
    private fun getLastData(id: Long): Data<Controller>? {
        return when (val value = getOrCreateSubject(id).value) {
            is Data -> value
            is ErrorStatus -> value.lastData
            is LoadingStatus -> value.lastData
            else -> null
        }
    }
    
    override suspend fun readState(controllerId: Long): String {
        delay(2000)
        (0..2).random().takeIf { it == 1 }?.let { throw Throwable("random error") }
        val newState = "${(10..30).random()} C"
        getOrCreateSubject(controllerId).apply {
            val currentValue = value
            if (currentValue is Data) {
                onNext(Data(currentValue.data.copy(state = newState)))
            }
        }
        return newState
    }
    
    override suspend fun get(id: Long): Controller {
        emitLoading(id)
        return retrofitFactory.createApi(ControllersApi::class.java)
            .runCatching { getDetails(id) }
            .onSuccess { emitData(id, it) }
            .onFailure { emitError(id, it) }
            .getOrThrow()
    }
}
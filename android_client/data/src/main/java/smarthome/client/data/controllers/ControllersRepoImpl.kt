package smarthome.client.data.controllers

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.delay
import smarthome.client.data.api.controllers.ControllersRepo
import smarthome.client.data.retrofit.RetrofitFactory
import smarthome.client.entity.Controller
import smarthome.client.util.*
import java.util.*

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
        delay(2000)
        (0..2).random().takeIf { it == 1 }?.let { throw Throwable("random error") }
        getOrCreateSubject(controllerId).apply {
            val currentValue = value
            if (currentValue is Data) {
                onNext(Data(currentValue.data.copy(state = state)))
            }
        }
        return state
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
        val subject = getOrCreateSubject(id)
        
        subject.onNext(LoadingStatus())
        return retrofitFactory.createApi(ControllersApi::class.java)
            .runCatching { getDetails(id) }
            .onSuccess { subject.onNext(Data(it)) }
            .onFailure { subject.onNext(ErrorStatus(it)) }
            .getOrThrow()
    }
}
package smarthome.client.data.controllers

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
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
package smarthome.raspberry.home.domain

import io.reactivex.Observable
import smarthome.raspberry.home.api.domain.GetHomeIdUseCase
import smarthome.raspberry.util.persistence.StorageHelper
import smarthome.raspberry.util.persistence.get

class GetHomeIdUseCaseImpl(private val storageHelper: StorageHelper): GetHomeIdUseCase {
    
    override fun execute(): Observable<String> {
        val homeId = try {
            storageHelper.get<String>(HOME_ID)
        } catch (e: IllegalArgumentException) {
            EMPTY_HOME_ID
        }
        
        return Observable.just(homeId)
    }
    
    companion object {
        internal const val HOME_ID = "HOME_ID"
        internal const val EMPTY_HOME_ID = ""
    }
}
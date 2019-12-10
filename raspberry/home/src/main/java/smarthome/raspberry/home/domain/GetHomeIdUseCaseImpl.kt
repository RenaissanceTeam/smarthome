package smarthome.raspberry.home.domain

import io.reactivex.Observable
import smarthome.raspberry.home.api.domain.GetHomeIdUseCase
import smarthome.raspberry.util.persistence.SharedPreferencesHelper

class GetHomeIdUseCaseImpl(private val sharedPreferencesHelper: SharedPreferencesHelper): GetHomeIdUseCase {
    
    override fun execute(): Observable<String> {
        return Observable.just(sharedPreferencesHelper.getString(HOME_ID, EMPTY_HOME_ID))
    }
    
    companion object {
        internal const val HOME_ID = "HOME_ID"
        internal const val EMPTY_HOME_ID = ""
    }
}
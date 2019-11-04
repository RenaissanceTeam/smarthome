package smarthome.raspberry.home_api.data

import io.reactivex.Observable

interface HomeInfoSource {
    fun getObservableUserId(): Observable<String>
    fun getObservableHomeId(): Observable<String>
}
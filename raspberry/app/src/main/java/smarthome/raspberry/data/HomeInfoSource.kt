package smarthome.raspberry.data

import io.reactivex.Observable

interface HomeInfoSource {
    fun getObservableUserId(): Observable<String>
    fun getObservableHomeId(): Observable<String>
}
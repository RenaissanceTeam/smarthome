package smarthome.client.data.notifications

import android.annotation.SuppressLint
import android.content.SharedPreferences
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.runBlocking
import smarthome.client.data.api.notifications.NotificationRepository
import smarthome.client.data.notifications.api.NotificationApi
import smarthome.client.data.notifications.dto.TokenDto
import smarthome.client.data.retrofit.RetrofitFactory
import smarthome.client.domain.api.auth.usecases.ObserveAuthenticationStatusUseCase
import smarthome.client.util.Data
import smarthome.client.util.DataStatus

class NotificationRepositoryImpl(
        private val retrofitFactory: RetrofitFactory,
        private val observeAuthenticationStatusUseCase: ObserveAuthenticationStatusUseCase,
        private val notificationPreferences: SharedPreferences
) : NotificationRepository {
    private val notificationToken = BehaviorSubject.createDefault(
            DataStatus.from(notificationPreferences.getString("token", null))
    )

    init {
        saveTokenWhenHaveAuth()
    }

    @SuppressLint("CheckResult")
    private fun saveTokenWhenHaveAuth() {
        Observable.combineLatest<Boolean, String, String>(
                observeAuthenticationStatusUseCase.execute().filter { it },
                notificationToken.filter { it is Data }.map { (it as Data).data },
                BiFunction { _, token -> token }
        )
                .subscribeOn(Schedulers.io())
                .subscribe { runBlocking { saveToServer(it) } }
    }

    override suspend fun save(token: String) {
        notificationPreferences.edit().putString("token", token).apply()
        notificationToken.onNext(DataStatus.from(token))
    }

    private suspend fun saveToServer(token: String) {
        retrofitFactory.createApi(NotificationApi::class.java).save(TokenDto(token))
    }
}
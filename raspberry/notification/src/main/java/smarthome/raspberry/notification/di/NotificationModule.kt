package smarthome.raspberry.notification.di

import com.google.gson.Gson
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.experimental.builder.factoryBy
import retrofit2.Retrofit
import smarthome.raspberry.notification.R
import smarthome.raspberry.notification.api.domain.ComposeDataNotificationUseCase
import smarthome.raspberry.notification.api.domain.SendDataNotificationUseCase
import smarthome.raspberry.notification.data.FcmSenderApi
import smarthome.raspberry.notification.data.command.SendFcmCommand
import smarthome.raspberry.notification.data.command.SendFcmCommandImpl
import smarthome.raspberry.notification.data.mapper.NotificationToRequestBodyMapper
import smarthome.raspberry.notification.data.mapper.NotificationToRequestBodyMapperImpl
import smarthome.raspberry.notification.domain.ComposeDataNotificationUseCaseImpl
import smarthome.raspberry.notification.domain.SendDataNotificationUseCaseImpl
import smarthome.raspberry.util.ResourceProvider

private val domain = module {
    factoryBy<ComposeDataNotificationUseCase, ComposeDataNotificationUseCaseImpl>()
    factoryBy<SendDataNotificationUseCase, SendDataNotificationUseCaseImpl>()
}

private val data = module {
    factory<NotificationToRequestBodyMapper> {
        NotificationToRequestBodyMapperImpl(get(named("notification")))
    }
    factoryBy<SendFcmCommand, SendFcmCommandImpl>()
    single<Gson>(named("notification")) { Gson() }
    factory<FcmSenderApi> {
        Retrofit.Builder()
                .baseUrl(get<ResourceProvider>().resources.getString(R.string.fcm_sender_url))
                .build()
                .create(FcmSenderApi::class.java)
    }
}

val notificationModule = listOf(domain, data)
package smarthome.client.data.notifications

import android.content.SharedPreferences
import com.nhaarman.mockitokotlin2.*
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import smarthome.client.data.notifications.api.NotificationApi
import smarthome.client.data.retrofit.RetrofitFactory
import smarthome.client.domain.api.auth.usecases.ObserveAuthenticationStatusUseCase
import smarthome.client.util.trampoline

class NotificationRepositoryImplTest {
    private lateinit var repo: NotificationRepositoryImpl
    private lateinit var prefs: SharedPreferences
    private lateinit var retrofitFactory: RetrofitFactory
    private lateinit var getAuthenticationStatusUseCase: ObserveAuthenticationStatusUseCase
    private lateinit var auth: PublishSubject<Boolean>
    private lateinit var api: NotificationApi

    @Before
    fun setUp() {
        prefs = mock {
            on { edit() }.then {
                mock<SharedPreferences.Editor> {
                    on { putString(any(), any()) }.then { this.mock }
                }
            }
        }
        api = mock {}
        retrofitFactory = mock {
            on { createApi(NotificationApi::class.java) }.then { api }
        }
        auth = PublishSubject.create()
        getAuthenticationStatusUseCase = mock {
            on { execute() }.then { auth }
        }

        repo = NotificationRepositoryImpl(retrofitFactory, getAuthenticationStatusUseCase, prefs)

        trampoline()
    }

    @Test
    fun `when have no auth and new token should not push it to server`() {
        runBlocking {
            auth.onNext(false)

            repo.save("new")

            verify(api, never()).save(any())
        }
    }

    @Test
    fun `when have auth and new token should push it to server`() {
        runBlocking {
            auth.onNext(true)

            repo.save("new")

            verify(api).save(argThat { token == "new" })
        }
    }

    @Test
    fun `should push saved token after getting auth`() {
        runBlocking {
            repo.save("new")
            verify(api, never()).save(any())

            auth.onNext(true)
            verify(api).save(argThat { token == "new" })
        }
    }

    @Test
    fun `should resend token after new auth`() {
        runBlocking {
            repo.save("new")

            auth.onNext(true)
            auth.onNext(false)
            auth.onNext(true)

            verify(api, times(2)).save(argThat { token == "new" })
        }
    }

    @Test
    fun `when have auth but no token should not send `() {
        runBlocking {
            auth.onNext(true)

            verify(api, never()).save(any())
        }
    }

}
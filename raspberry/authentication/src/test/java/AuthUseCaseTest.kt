package smarthome.raspberry.domain.usecases

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Test
import smarthome.raspberry.domain.AuthRepo

class AuthUseCaseTest {
    private val authRepo = mock<AuthRepo>()
    private val authUseCase = AuthUseCase(authRepo)
    private val authSubject = PublishSubject.create<AuthUseCase.AuthStatus>()
    @Before
    fun setUp() {
        whenever(authRepo.getAuthStatus()).then { authSubject }
    }

    @Test
    fun usedNotSignedIn_getAuthStatus_StreamEmitNotSignedIn() {
        val testObserver = authUseCase.isAuthenticated().test()
        authSubject.onNext(AuthUseCase.AuthStatus.NOT_SIGNED_IN)

        testObserver.assertValue(AuthUseCase.AuthStatus.NOT_SIGNED_IN)
    }
}
package smarthome.raspberry.authentication.domain

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test
import smarthome.raspberry.authentication.api.domain.GetUserInfoUseCase
import smarthome.raspberry.authentication.data.AuthRepo

class GetUserInfoUseCaseImplTest {
    private lateinit var authRepo: AuthRepo
    private lateinit var useCase: GetUserInfoUseCase

    @Before
    fun setUp() {
        authRepo = mock {}
        useCase = GetUserInfoUseCaseImpl(authRepo)
    }

    @Test
    fun `when execute should call repo for user info`() {
        useCase.execute()

        verify(authRepo).getUser()
    }
}
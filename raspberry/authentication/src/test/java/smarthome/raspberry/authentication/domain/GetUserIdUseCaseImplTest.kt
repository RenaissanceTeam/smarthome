package smarthome.raspberry.authentication.domain

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test
import smarthome.raspberry.authentication.api.domain.GetUserIdUseCase
import smarthome.raspberry.authentication.data.AuthRepo

class GetUserIdUseCaseImplTest {
    private lateinit var authRepo: AuthRepo
    private lateinit var useCase: GetUserIdUseCase

    @Before
    fun setUp() {
        authRepo = mock {}
        useCase = GetUserIdUseCaseImpl(authRepo)
    }

    @Test
    fun `when execute should call repo for user id`() {
        useCase.execute()

        verify(authRepo).getUserId()
    }
}
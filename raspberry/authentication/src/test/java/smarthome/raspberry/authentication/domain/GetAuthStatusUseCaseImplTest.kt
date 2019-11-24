package smarthome.raspberry.authentication.domain

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test
import smarthome.raspberry.authentication.api.domain.GetAuthStatusUseCase
import smarthome.raspberry.authentication.data.AuthRepo

class GetAuthStatusUseCaseImplTest {
    private lateinit var authRepo: AuthRepo
    private lateinit var authUseCase: GetAuthStatusUseCase

    @Before
    fun setUp() {
        authRepo = mock {}
        authUseCase = GetAuthStatusUseCaseImpl(authRepo)
    }
    
    @Test
    fun `when execute should call repo for auth status`() {
        authUseCase.execute()
        verify(authRepo).getAuthStatus()
    }

}
package smarthome.raspberry.authentication.domain

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import smarthome.raspberry.authentication.api.domain.SignUpUseCase
import smarthome.raspberry.authentication.api.domain.entity.Credentials
import smarthome.raspberry.authentication.api.domain.exceptions.UserExistsException
import smarthome.raspberry.authentication.data.AuthRepo
import kotlin.test.assertFailsWith

class SignUpUseCaseImplTest {
    private lateinit var useCase: SignUpUseCase
    
    private lateinit var repo: AuthRepo
    
    @Before
    fun setUp() {
        repo = mock {}
        useCase = SignUpUseCaseImpl(repo)
    }
    
    @Test
    fun `when there exists user with same login should throw`() {
        val login = "a"
        val cred = Credentials(login, "pass")
        whenever(repo.checkUserExists(login)).thenReturn(true)
        assertFailsWith<UserExistsException> { useCase.execute(cred) }
    }
}
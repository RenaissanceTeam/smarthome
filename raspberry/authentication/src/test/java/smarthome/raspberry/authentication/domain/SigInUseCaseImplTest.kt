package smarthome.raspberry.authentication.domain

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Test
import smarthome.raspberry.authentication.api.domain.SignInUseCase
import smarthome.raspberry.authentication.api.domain.entity.Credentials
import smarthome.raspberry.authentication.api.domain.exceptions.UserExistsException
import smarthome.raspberry.authentication.data.AuthRepo
import kotlin.test.assertFailsWith

class SigInUseCaseImplTest {
    private lateinit var useCase: SignInUseCase
    
    private lateinit var repo: AuthRepo
    
    @Before
    fun setUp() {
        repo = mock {}
        useCase = SignInUseCaseImpl(repo)
    }
    
    @Test
    fun `when repo contains user with same login should throw`() {
        
        val login = "s"
        whenever(repo.findByLogin(login)).thenReturn(mock())
        assertFailsWith<UserExistsException> {
            useCase.execute(Credentials(login, "sfd"))
        }
    }
    
    @Test
    fun `when no saved user with same login should save new user`() {
        val login = "s"
        val password = "ssadf"
        whenever(repo.findByLogin(login)).thenReturn(null)
        
        useCase.execute(Credentials(login, password))
        verify(repo.save())
    }
}
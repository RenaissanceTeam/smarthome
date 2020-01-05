package smarthome.raspberry.authentication.domain

import com.nhaarman.mockitokotlin2.argThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Test
import smarthome.raspberry.authentication.api.domain.SignUpUseCase
import smarthome.raspberry.authentication.api.domain.entity.Credentials
import smarthome.raspberry.authentication.api.domain.entity.RegistrationInfo
import smarthome.raspberry.authentication.api.domain.exceptions.UserExistsException
import smarthome.raspberry.authentication.data.AuthRepo
import smarthome.raspberry.authentication.data.UserRoleRepo
import smarthome.raspberry.authentication.domain.entity.User
import smarthome.raspberry.authentication.domain.entity.UserRoles
import kotlin.test.assertFailsWith

class SignUpUseCaseImplTest {
    private lateinit var useCase: SignUpUseCase
    
    private lateinit var authRepo: AuthRepo
    private lateinit var roleRepo: UserRoleRepo
    
    @Before
    fun setUp() {
        authRepo = mock {}
        roleRepo = mock {}
        useCase = SignUpUseCaseImpl(authRepo, roleRepo)
    }
    
    @Test
    fun `when there exists user with same login should throw`() {
        val login = "a"
        val cred = Credentials(login, "pass")
        whenever(authRepo.findByUsername(login)).thenReturn(mock())
        assertFailsWith<UserExistsException> { useCase.execute(RegistrationInfo(cred, "")) }
    }
    
    @Test
    fun `when no saved user should create user and role and save to repos`() {
        val login = "a"
        val pass = "pass"
        val role = "role"
        val cred = Credentials(login, pass)
        
        whenever(authRepo.findByUsername(login)).thenReturn(null)
        
        useCase.execute(RegistrationInfo(cred, role))
        
        verify(authRepo).save<User>(argThat { this.username == login && this.password == pass })
        verify(roleRepo).save<UserRoles>(argThat { this.username == login && this.roles == role })
    }
}
package smarthome.raspberry.authentication.data

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import smarthome.raspberry.authentication.api.domain.AuthStatus
import smarthome.raspberry.authentication.api.domain.Credentials
import smarthome.raspberry.authentication.api.domain.User
import smarthome.raspberry.authentication.api.domain.exceptions.NotSignedInException
import smarthome.raspberry.authentication.data.command.SignInCommand
import smarthome.raspberry.authentication.data.command.SignOutCommand
import smarthome.raspberry.authentication.data.query.GetUserQuery

class AuthRepoImplTest {
    
    private lateinit var authRepo: AuthRepo
    private lateinit var signInCommand: SignInCommand
    private lateinit var signOutCommand: SignOutCommand
    private lateinit var getUserQuery: GetUserQuery
    
    
    @Before
    fun setUp() {
        signInCommand = mock {}
        signOutCommand = mock {}
        getUserQuery = mock {
            on { execute() }.then { mock<User> {} }
        }
        
        authRepo = AuthRepoImpl(signInCommand, signOutCommand, getUserQuery)
    }
    
    @Test
    fun `when user query returns user should emit on user and auth status subjects`() {
        val user = User("", "")
        whenever(getUserQuery.execute()).then { user }
    
        authRepo = AuthRepoImpl(signInCommand, signOutCommand, getUserQuery)
        authRepo.getAuthStatus().test().assertValue { it == AuthStatus.SIGNED_IN }
        assertThat(authRepo.getUser()).isEqualTo(user)
    }
    
    @Test
    fun `when user query fails should emit fail on auth status`() {
        whenever(getUserQuery.execute()).then { throw NotSignedInException() }
    
        authRepo = AuthRepoImpl(signInCommand, signOutCommand, getUserQuery)
        authRepo.getAuthStatus().test().assertValue { it == AuthStatus.NOT_SIGNED_IN }
    }
    
    @Test
    fun `when sign in successful should emit user and auth status`() {
        val credentials = mock<Credentials> {}
        val user = User("", "")
    
        runBlocking {
            whenever(signInCommand.execute(credentials)).then { user }
            authRepo.signIn(credentials)
        }
        
        authRepo.getAuthStatus().test().assertValue { it == AuthStatus.SIGNED_IN }
        assertThat(authRepo.getUser()).isEqualTo(user)
    }
    
    @Test
    fun `when sign out should emit not signed in status`() {
        runBlocking {
            authRepo.signOut()
        }
        authRepo.getAuthStatus().test().assertValue { it == AuthStatus.NOT_SIGNED_IN }
    }
}
package smarthome.client.domain.auth.usecases

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import smarthome.client.data.api.auth.LoginCommand
import smarthome.client.domain.api.auth.usecases.LoginUseCase
import smarthome.client.domain.api.auth.usecases.SaveNewTokenUseCase

class LoginUseCaseImplTest {
    private lateinit var useCase: LoginUseCase
    private lateinit var command: LoginCommand
    private lateinit var saveNewTokenUseCase: SaveNewTokenUseCase
    
    @Before
    fun setUp() {
        command = mock {}
        useCase = LoginUseCaseImpl(command)
    }
    
    @Test
    fun `when get token should save it with save new token use case`() {
        val login = "l"
        val pass = "p"
        val token = "token"
        runBlocking {
            whenever(command.run(login, pass)).then { token }
            useCase.execute(login, pass)
        }
        verify(saveNewTokenUseCase).execute(token)
    }
}
package smarthome.client.domain.homeserver.usecases

import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import smarthome.client.data.api.homeserver.HomeServerRepo
import smarthome.client.domain.api.auth.usecases.LogoutUseCase
import smarthome.client.domain.api.homeserver.usecases.ChangeHomeServerUrlUseCase
import smarthome.client.entity.HomeServer
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ChangeHomeServerUrlUseCaseImplTest {
    private lateinit var uc: ChangeHomeServerUrlUseCase
    private lateinit var repo: HomeServerRepo
    private lateinit var logoutUseCase: LogoutUseCase

    @Before
    fun setUp() {
        repo = mock {}
        logoutUseCase = mock {}

        uc = ChangeHomeServerUrlUseCaseImpl(repo, logoutUseCase)
    }

    @Test
    fun `when no current active should save new`() {
        runBlocking {
            whenever(repo.getCurrentActive()).then { null }
            whenever(repo.getByUrl(any())).then { null }

            uc.execute("some")
            verify(repo).save(argThat { url == "some" })
        }
    }

    @Test
    fun `when current active has same url should do nothing`() {
        runBlocking {
            val server = HomeServer(url = "some", active = true)

            whenever(repo.getCurrentActive()).then { server }
            whenever(repo.getByUrl(server.url)).then { server }

            uc.execute(server.url)

            verify(repo).getCurrentActive()
            verify(repo).getByUrl(server.url)
            verifyNoMoreInteractions(repo)
        }
    }

    @Test
    fun `when has other url than active and no saved item with the url should save new`() {
        runBlocking {
            val server = HomeServer(id = 111, url = "some", active = true)

            whenever(repo.getCurrentActive()).then { server }
            whenever(repo.getByUrl("new")).then { null }

            uc.execute("new")

            verify(repo).save(argThat { url == "new" && active })
            verify(repo).update(argThat { id == server.id && !active })
        }
    }

    @Test
    fun `should return false if saving same url`() {
        runBlocking {
            val server = HomeServer(url = "some", active = true)

            whenever(repo.getCurrentActive()).then { server }
            whenever(repo.getByUrl(server.url)).then { server }

            assertFalse(uc.execute(server.url))
        }
    }

    @Test
    fun `when saving new server should return true`() {
        runBlocking {
            val server = HomeServer(url = "some", active = true)

            whenever(repo.getCurrentActive()).then { server }
            whenever(repo.getByUrl(server.url)).then { server }

            assertTrue(uc.execute("new"))

            whenever(repo.getByUrl("new")).then { null }
            assertTrue(uc.execute("new"))

            whenever(repo.getCurrentActive()).then { null }
            assertTrue(uc.execute("new"))
        }
    }

    @Test
    fun `when changed server should logout`() {
        runBlocking {
            val server = HomeServer(url = "some", active = true)

            whenever(repo.getCurrentActive()).then { server }
            whenever(repo.getByUrl(server.url)).then { server }

            assertTrue(uc.execute("new"))
            verify(logoutUseCase).execute()
        }
    }
}
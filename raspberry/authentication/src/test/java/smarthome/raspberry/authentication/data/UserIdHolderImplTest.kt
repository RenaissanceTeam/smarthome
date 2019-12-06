package smarthome.raspberry.authentication.data

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import io.reactivex.subjects.ReplaySubject
import org.junit.Before
import org.junit.Test
import smarthome.library.datalibrary.api.boundary.UserIdHolder
import smarthome.raspberry.authentication.api.domain.GetUserIdUseCase

class UserIdHolderImplTest {
    private lateinit var holder: UserIdHolder
    private lateinit var getUserIdUserIdUseCase: GetUserIdUseCase

    @Before
    fun setup() {
        getUserIdUserIdUseCase = mock {  }
        holder = UserIdHolderImpl(getUserIdUserIdUseCase)
    }

    @Test
    fun `when get() should observe user id with getUserIdUseCase`() {
        whenever(getUserIdUserIdUseCase.execute()).then { Observable.empty<String>() }
        holder.get()

        verify(getUserIdUserIdUseCase).execute()
    }

    @Test
    fun `when emit user id should hold and return it`() {
        val userId = "i"
        whenever(getUserIdUserIdUseCase.execute()).then { Observable.just(userId) }
        assertThat(holder.get()).isEqualTo(userId)
    }

    @Test
    fun `when emit two user ids should change the holded value starting from empty string`() {
        val firstUserId = "1"
        val secondUserId = "2"
        val subject = ReplaySubject.create<String>()
        whenever(getUserIdUserIdUseCase.execute()).then { subject }

        assertThat(holder.get()).isEmpty()

        subject.onNext(firstUserId)
        assertThat(holder.get()).isEqualTo(firstUserId)
        assertThat(holder.get()).isEqualTo(firstUserId)

        subject.onNext(secondUserId)
        assertThat(holder.get()).isEqualTo(secondUserId)
    }
}
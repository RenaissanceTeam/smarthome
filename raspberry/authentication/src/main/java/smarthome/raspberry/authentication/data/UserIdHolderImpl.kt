package smarthome.raspberry.authentication.data

import io.reactivex.subjects.BehaviorSubject
import smarthome.library.datalibrary.api.boundary.UserIdHolder
import smarthome.raspberry.authentication.api.domain.GetUserIdUseCase

class UserIdHolderImpl(
        private val getUserIdUseCase: GetUserIdUseCase
) : UserIdHolder {
    private val userId: BehaviorSubject<String> by lazy {
        val subject = BehaviorSubject.create<String>()
        getUserIdUseCase.execute().subscribe { subject.onNext(it) }
        subject
    }

    override fun get(): String {
        return userId.value.orEmpty()
    }
}
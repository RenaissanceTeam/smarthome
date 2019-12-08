package smarthome.raspberry.authentication.data

import smarthome.library.common.util.delegates.BehaviourSubjectFromObservable
import smarthome.library.datalibrary.api.boundary.UserIdHolder
import smarthome.raspberry.authentication.api.domain.GetUserIdUseCase

class UserIdHolderImpl(getUserIdUseCase: GetUserIdUseCase) : UserIdHolder {
    private val userId by BehaviourSubjectFromObservable(getUserIdUseCase::execute)

    override fun get() = userId.value.orEmpty()
}
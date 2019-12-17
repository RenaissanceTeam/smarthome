package smarthome.raspberry.home.data

import smarthome.library.common.util.delegates.BehaviourSubjectFromObservable
import smarthome.library.datalibrary.api.boundary.HomeIdHolder
import smarthome.raspberry.home.api.domain.ObserveHomeIdUseCase

class HomeIdHolderImpl(observeHomeIdUseCase: ObserveHomeIdUseCase): HomeIdHolder {
    private val homeId by BehaviourSubjectFromObservable(observeHomeIdUseCase::execute)

    override fun get() = homeId.value.orEmpty()
}
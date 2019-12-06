package smarthome.raspberry.home.data

import smarthome.library.common.util.delegates.BehaviourSubjectFromObservable
import smarthome.library.datalibrary.api.boundary.HomeIdHolder
import smarthome.raspberry.home.api.domain.GetHomeIdUseCase

class HomeIdHolderImpl(getHomeIdUseCase: GetHomeIdUseCase): HomeIdHolder {
    private val homeId by BehaviourSubjectFromObservable(getHomeIdUseCase::execute)

    override fun get() = homeId.value.orEmpty()
}
package smarthome.client.domain.conrollers.usecases

import io.reactivex.Observable
import smarthome.client.data.api.controllers.ControllersRepo
import smarthome.client.domain.api.conrollers.usecases.ObserveControllerUseCase
import smarthome.client.entity.Controller
import smarthome.client.util.DataStatus

class ObserveControllerUseCaseImpl(
    private val repo: ControllersRepo
) : ObserveControllerUseCase {
    override fun execute(): Observable<DataStatus<Controller>> {
        TODO()
    }
}
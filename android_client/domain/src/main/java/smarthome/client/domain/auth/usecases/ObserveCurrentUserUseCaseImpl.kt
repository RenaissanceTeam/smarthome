package smarthome.client.domain.auth.usecases

import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import smarthome.client.data.api.auth.UserRepository
import smarthome.client.domain.api.auth.usecases.ObserveCurrentUserUseCase
import smarthome.client.domain.api.homeserver.usecases.ObserveActiveHomeServerUseCase
import smarthome.client.entity.User
import smarthome.client.util.DataStatus
import smarthome.client.util.EMPTY

class ObserveCurrentUserUseCaseImpl(
    private val userRepository: UserRepository,
    private val observeActiveHomeServerUseCase: ObserveActiveHomeServerUseCase

) : ObserveCurrentUserUseCase {
    override fun execute(): Observable<DataStatus<User>> {
        return Observables.combineLatest(
            observeActiveHomeServerUseCase.execute(),
            userRepository.get()
        ) { server, users ->
            if (server.status == EMPTY) return@combineLatest DataStatus.from<User>(null)
            
            val serverId = server.data!!.id
            DataStatus.from(users.find { it.serverId == serverId})
        }
    }
}
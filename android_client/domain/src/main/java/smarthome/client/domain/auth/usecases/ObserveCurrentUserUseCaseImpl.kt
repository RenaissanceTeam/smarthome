package smarthome.client.domain.auth.usecases

import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import smarthome.client.data.api.auth.UserRepository
import smarthome.client.domain.api.auth.usecases.ObserveCurrentUserUseCase
import smarthome.client.domain.api.homeserver.usecases.ObserveActiveHomeServerUseCase
import smarthome.client.entity.User
import smarthome.client.util.*

class ObserveCurrentUserUseCaseImpl(
    private val userRepository: UserRepository,
    private val observeActiveHomeServerUseCase: ObserveActiveHomeServerUseCase
) : ObserveCurrentUserUseCase {
    override fun execute(): Observable<DataStatus<User>> {
        return Observables.combineLatest(
            observeActiveHomeServerUseCase.execute(),
            userRepository.get()
        ) { server, users ->
            when (server) {
                is Data -> {
                    when (val user = users.find { it.serverId == server.data.id }) {
                        null -> EmptyStatus<User>()
                        else -> Data(user)
                    }
                }
                is LoadingStatus -> LoadingStatus()
                is ErrorStatus -> ErrorStatus(server.cause)
                is EmptyStatus -> EmptyStatus()
            }
        }
    }
}
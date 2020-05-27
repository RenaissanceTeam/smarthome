package smarthome.raspberry.scripts.data

import io.reactivex.disposables.Disposable
import org.springframework.stereotype.Component
import smarthome.raspberry.scripts.api.data.RegisteredProtocolsRepository
import smarthome.raspberry.util.has

@Component
class RegisteredProtocolsRepositoryImpl : RegisteredProtocolsRepository {
    private val protocols = mutableMapOf<Long, Disposable>()

    override fun unregister(scriptId: Long) {
        protocols.remove(scriptId)?.also { it.dispose() }
    }

    override fun register(scriptId: Long, disposable: Disposable) {
        protocols[scriptId] = disposable
    }

    override fun isRegistered(id: Long) = protocols has id
}
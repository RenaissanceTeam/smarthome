package smarthome.raspberry.scripts.data

import io.reactivex.disposables.Disposable
import org.springframework.stereotype.Component
import smarthome.raspberry.scripts.api.data.RegisteredProtocolsRepository
import smarthome.raspberry.scripts.api.domain.GetScriptByIdUseCase
import smarthome.raspberry.scripts.api.domain.RegisterScriptProtocolUseCase
import smarthome.raspberry.util.has

@Component
class RegisteredProtocolsRepositoryImpl(
        private val registerScriptProtocolUseCase: RegisterScriptProtocolUseCase,
        private val getScriptByIdUseCase: GetScriptByIdUseCase
) : RegisteredProtocolsRepository {
    private val protocols = mutableMapOf<Long, Disposable>()

    override fun register(scriptId: Long) {
        if (protocols has scriptId) unregister(scriptId)

        registerScriptProtocolUseCase.execute(getScriptByIdUseCase.execute(scriptId))
    }

    override fun unregister(scriptId: Long) {
        protocols.remove(scriptId)?.also { it.dispose() }
    }
}
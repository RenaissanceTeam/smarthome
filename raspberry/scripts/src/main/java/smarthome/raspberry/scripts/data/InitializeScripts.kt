package smarthome.raspberry.scripts.data

import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component
import smarthome.raspberry.scripts.api.data.RegisteredProtocolsRepository
import smarthome.raspberry.scripts.api.domain.usecase.GetAllScriptsUseCase

@Component
class InitializeScripts(
        private val getAllScriptsUseCase: GetAllScriptsUseCase,
        private val registeredProtocolsRepository: RegisteredProtocolsRepository
) : InitializingBean {


    override fun afterPropertiesSet() {
        // register all enabled script protocols ?
        getAllScriptsUseCase.execute()
                .filter { it.enabled }
                .forEach { registeredProtocolsRepository.register(it.id) }
    }
}
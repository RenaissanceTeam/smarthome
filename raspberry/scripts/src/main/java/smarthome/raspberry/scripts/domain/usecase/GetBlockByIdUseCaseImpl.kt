package smarthome.raspberry.scripts.domain.usecase

import org.springframework.stereotype.Component
import smarthome.raspberry.entity.script.Block
import smarthome.raspberry.scripts.api.domain.usecase.GetBlockByIdUseCase
import smarthome.raspberry.scripts.data.BlockRepository
import smarthome.raspberry.util.exceptions.notFound

@Component
class GetBlockByIdUseCaseImpl(
        private val blockRepository: BlockRepository
) : GetBlockByIdUseCase {
    override fun execute(id: String): Block {
        return blockRepository.findById(id).runCatching { get() }.getOrElse { throw notFound }
    }
}
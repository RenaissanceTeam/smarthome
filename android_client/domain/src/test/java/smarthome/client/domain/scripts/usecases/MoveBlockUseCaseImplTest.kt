package smarthome.client.domain.scripts.usecases

import com.nhaarman.mockitokotlin2.*
import org.junit.Before
import org.junit.Test
import smarthome.client.data.api.scripts.SetupScriptRepo
import smarthome.client.domain.api.scripts.usecases.setup.MoveBlockUseCase
import smarthome.client.domain.scripts.usecases.setup.MoveBlockUseCaseImpl
import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.controller.ControllerBlock
import smarthome.client.entity.script.controller.ControllerBlockId
import smarthome.client.util.Position

data class MockControllerBlock(override val id: ControllerBlockId, override val position: Position) : ControllerBlock {
    override fun copyWithPosition(newPosition: Position): Block {
        return copy(position = newPosition)
    }
}

class MoveBlockUseCaseImplTest {
    
    private lateinit var useCase: MoveBlockUseCase
    private lateinit var repo: SetupScriptRepo
    
    @Before
    fun setUp() {
        repo = mock {}
        useCase = MoveBlockUseCaseImpl(repo)
    }
    
    @Test
    fun `when move block should find block with id and change its position`() {
        val blockId = ControllerBlockId(1)
        val newPosition = Position(22, 22)
        val id = ControllerBlockId(1)
        val block = MockControllerBlock(id, newPosition)
        
        whenever(repo.getBlocks(2)).then { listOf(block) }
        
        useCase.execute(2, blockId, newPosition)
        
        verify(repo).replaceBlock(any(), argThat { block.id == blockId && block.position == newPosition })
    }
}
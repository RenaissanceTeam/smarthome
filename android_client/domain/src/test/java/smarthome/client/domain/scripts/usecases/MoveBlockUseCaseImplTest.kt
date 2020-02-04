package smarthome.client.domain.scripts.usecases

import com.nhaarman.mockitokotlin2.argThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Test
import smarthome.client.data.api.scripts.ScriptGraphRepo
import smarthome.client.domain.api.scripts.usecases.MoveBlockUseCase
import smarthome.client.entity.script.Position
import smarthome.client.entity.script.controller.ControllerBlock
import smarthome.client.entity.script.controller.ControllerBlockId

class MoveBlockUseCaseImplTest {
    
    private lateinit var useCase: MoveBlockUseCase
    private lateinit var repo: ScriptGraphRepo
    
    @Before
    fun setUp() {
        repo = mock {}
        useCase = MoveBlockUseCaseImpl(repo)
    }
    
    @Test
    fun `when move block should find block with id and change its position`() {
        val blockId = ControllerBlockId(1)
        val newPosition = Position(22, 22)
        val block = ControllerBlock(1, newPosition)
        
        whenever(repo.getBlocks(2)).then { listOf(block) }
        
        useCase.execute(2, blockId, newPosition)
        
        verify(repo).replaceBlock(argThat { id == blockId && position == newPosition })
    }
}
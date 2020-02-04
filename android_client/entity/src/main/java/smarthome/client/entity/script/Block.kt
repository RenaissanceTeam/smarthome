package smarthome.client.entity.script

interface Block {
    val id: BlockId
    val position: Position
    
    fun copyWithPosition(newPosition: Position): Block
}
package smarthome.client.entity.script

data class BlockDependency(
    override val id: DependencyId,
    override val startBlock: BlockId,
    override val endBlock: BlockId
) : Dependency
package smarthome.client.entity.script

interface Dependency {
    val id: DependencyId
    val startBlock: BlockId
    val endBlock: BlockId
}
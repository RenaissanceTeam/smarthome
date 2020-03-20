package smarthome.client.entity.script

import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.dependency.Dependency

data class Script(
    val id: Long = 0,
    val name: String = "",
    val description: String = "",
    val blocks: List<Block> = listOf(),
    val dependencies: List<Dependency> = listOf()
)
package smarthome.raspberry.scripts.data

import org.springframework.data.jpa.repository.JpaRepository
import smarthome.raspberry.entity.script.Block
import smarthome.raspberry.entity.script.Dependency
import smarthome.raspberry.entity.script.Script

interface ScriptsRepository : JpaRepository<Script, Long> {
    fun findByBlocks(block: Block): Script?
}
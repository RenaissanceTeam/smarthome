package smarthome.raspberry.scripts.data

import org.springframework.data.jpa.repository.JpaRepository
import smarthome.raspberry.entity.script.Script

//import smarthome.library.common.scripts.Script
//
//interface ScriptsRepository {
//    val scripts: List<Script>
//
//    suspend fun save(newScript: Script)
//    suspend fun delete(script: Script)
//}

interface ScriptsRepository : JpaRepository<Script, Long>
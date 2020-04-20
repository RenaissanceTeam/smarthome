package smarthome.raspberry.scripts.data

import org.springframework.data.repository.reactive.RxJava2CrudRepository
import smarthome.raspberry.entity.script.Script

interface ScriptsRepository : RxJava2CrudRepository<Script, Long>
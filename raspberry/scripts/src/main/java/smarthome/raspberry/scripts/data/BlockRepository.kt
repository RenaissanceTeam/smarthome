package smarthome.raspberry.scripts.data

import org.springframework.data.repository.reactive.RxJava2CrudRepository
import smarthome.raspberry.entity.script.Block

interface BlockRepository : RxJava2CrudRepository<Block, String>
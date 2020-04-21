package smarthome.raspberry.scripts.data

import org.springframework.data.jpa.repository.JpaRepository
import smarthome.raspberry.entity.script.Block

interface BlockRepository : JpaRepository<Block, String>
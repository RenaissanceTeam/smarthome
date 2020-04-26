package smarthome.raspberry.scripts.api.domain

import smarthome.raspberry.entity.script.Block

interface GetBlockByIdUseCase {
    fun execute(id: String): Block
}
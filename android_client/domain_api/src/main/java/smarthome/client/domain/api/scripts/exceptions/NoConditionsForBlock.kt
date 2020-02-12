package smarthome.client.domain.api.scripts.exceptions

import smarthome.client.entity.script.block.BlockId

class NoConditionsForBlock(blockId: BlockId, msg: String = "") :
    Throwable("$msg Reason: There are no available conditions for block $blockId")


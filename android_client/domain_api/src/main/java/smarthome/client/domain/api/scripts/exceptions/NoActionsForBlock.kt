package smarthome.client.domain.api.scripts.exceptions

import smarthome.client.entity.script.block.BlockId

class NoActionsForBlock(blockId: BlockId, msg: String = "") :
    Throwable("$msg Reason: There are no available actions for block $blockId")
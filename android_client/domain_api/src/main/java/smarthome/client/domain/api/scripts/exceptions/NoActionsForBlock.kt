package smarthome.client.domain.api.scripts.exceptions

class NoActionsForBlock(uuid: String, msg: String = "") :
    Throwable("$msg Reason: There are no available actions for block $uuid")
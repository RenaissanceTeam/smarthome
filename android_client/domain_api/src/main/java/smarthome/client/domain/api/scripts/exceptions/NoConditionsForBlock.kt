package smarthome.client.domain.api.scripts.exceptions

class NoConditionsForBlock(uuid: String, msg: String = "") :
    Throwable("$msg Reason: There are no available conditions for block $uuid")


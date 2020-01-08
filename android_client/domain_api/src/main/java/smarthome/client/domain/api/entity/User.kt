package smarthome.client.domain.api.entity

data class User (
    val username: String
)

val NOT_SIGNED_IN = User("")
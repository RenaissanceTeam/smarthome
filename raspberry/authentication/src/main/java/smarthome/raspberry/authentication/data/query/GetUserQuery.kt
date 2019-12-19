package smarthome.raspberry.authentication.data.query

import smarthome.raspberry.authentication.api.domain.User

interface GetUserQuery {
    fun execute(): User
}
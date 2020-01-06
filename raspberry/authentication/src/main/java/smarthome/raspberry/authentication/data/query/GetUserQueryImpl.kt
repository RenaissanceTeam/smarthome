package smarthome.raspberry.authentication.data.query

import smarthome.raspberry.authentication.api.domain.exceptions.NotSignedInException


class GetUserQueryImpl(
) : GetUserQuery {
    override fun execute() = TODO() ?: throw NotSignedInException()
}
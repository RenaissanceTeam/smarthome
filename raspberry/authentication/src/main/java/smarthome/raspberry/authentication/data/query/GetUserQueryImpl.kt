package smarthome.raspberry.authentication.data.query

import com.google.firebase.auth.FirebaseAuth
import smarthome.raspberry.authentication.api.domain.exceptions.NotSignedInException
import smarthome.raspberry.authentication.data.mapper.FirebaseUserToUserMapper

class GetUserQueryImpl(
    private val auth: FirebaseAuth,
    private val mapper: FirebaseUserToUserMapper
) : GetUserQuery {
    override fun execute() = mapper.map(auth.currentUser ?: throw NotSignedInException())
}
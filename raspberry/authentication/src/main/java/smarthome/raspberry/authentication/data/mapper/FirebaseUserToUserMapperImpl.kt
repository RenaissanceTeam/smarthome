package smarthome.raspberry.authentication.data.mapper

import com.google.firebase.auth.FirebaseUser
import smarthome.raspberry.authentication.api.domain.User

class FirebaseUserToUserMapperImpl : FirebaseUserToUserMapper {
    override fun map(user: FirebaseUser): User {
        return User(
                user.email.orEmpty(),
                user.uid
        )
    }
}
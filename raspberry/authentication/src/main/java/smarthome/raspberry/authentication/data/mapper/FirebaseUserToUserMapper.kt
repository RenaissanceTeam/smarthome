package smarthome.raspberry.authentication.data.mapper

import com.google.firebase.auth.FirebaseUser
import smarthome.raspberry.authentication.api.domain.User

interface FirebaseUserToUserMapper {
    fun map(user: FirebaseUser): User
}
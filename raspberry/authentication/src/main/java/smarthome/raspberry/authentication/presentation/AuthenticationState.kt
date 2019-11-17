package smarthome.raspberry.authentication.presentation

import smarthome.raspberry.authentication.api.domain.User

interface AuthenticationState {
    object Loading : AuthenticationState
    object Empty : AuthenticationState
    class Authenticated(val user: User) :
            AuthenticationState
    class Error(val reason: String) :
            AuthenticationState
}
package smarthome.raspberry.authentication.presentation

interface AuthenticationView {
    fun setState(state: AuthenticationState)
    fun startAuthentication()
    fun showDeleteAllConfirmationDialog(action: () -> Unit)
}


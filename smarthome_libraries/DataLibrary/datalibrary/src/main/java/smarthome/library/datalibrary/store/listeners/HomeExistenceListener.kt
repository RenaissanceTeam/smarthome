package smarthome.library.datalibrary.store.listeners

interface HomeExistenceListener {
    fun onHomeAlreadyExists()

    fun onHomeDoesNotExist()
}
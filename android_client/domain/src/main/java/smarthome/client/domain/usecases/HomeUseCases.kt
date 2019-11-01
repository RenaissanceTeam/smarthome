package smarthome.client.domain.usecases

import smarthome.client.data_api.HomeRepository

class HomeUseCases(private val repository: HomeRepository) {
    suspend fun chooseHomeId(homeIds: MutableList<String>): String {
        val savedHomeId = repository.getSavedHomeId() ?: homeIds.first()

        if (savedHomeId in homeIds) return savedHomeId
        else TODO("we have saved home id, but user's home ids in firestore don't contain it. Should show this info")
    }

}
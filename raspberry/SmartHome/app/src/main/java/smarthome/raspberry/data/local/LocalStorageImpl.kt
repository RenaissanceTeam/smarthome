package smarthome.raspberry.data.local

import android.content.Context
import smarthome.raspberry.data.LocalStorage
import smarthome.raspberry.utils.SharedPreferencesHelper

class LocalStorageImpl(private val context: Context): LocalStorage {

    private val sharedPreferencesHelper = SharedPreferencesHelper.getInstance(context)
    private val input: LocalStorageInput = TODO()
    private val output: LocalStorageOutput = TODO()

    override suspend fun getHomeId(): String {
        if (!sharedPreferencesHelper.isHomeIdExists()) {
            saveNewHomeId()
        }
        return sharedPreferencesHelper.getHomeId()
    }

    private suspend fun saveNewHomeId() {
        val homeId = input.generateHomeId()
        sharedPreferencesHelper.setHomeId(homeId)

        output.createHome(homeId)
    }
}
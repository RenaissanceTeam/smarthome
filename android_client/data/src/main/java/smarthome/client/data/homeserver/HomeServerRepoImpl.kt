package smarthome.client.data.homeserver

import android.content.Context
import com.google.gson.Gson
import smarthome.client.data.api.homeserver.HomeServerRepo
import smarthome.client.domain.api.entity.HomeServer
import smarthome.client.domain.api.homeserver.usecases.entity.NoHomeServerException

class HomeServerRepoImpl(
    private val context: Context,
    private val gson: Gson
) : HomeServerRepo {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    override fun get(): HomeServer {
        return try {
            gson.fromJson(prefs.getString(HOMESERVER_KEY, ""), HomeServer::class.java)
        } catch (e: Throwable) {
            throw NoHomeServerException()
        }
    }
    
    override fun save(homeServer: HomeServer) {
        prefs.edit().putString(HOMESERVER_KEY, gson.toJson(homeServer)).apply()
    }
    
    companion object {
        const val PREFS_NAME = "homeserver"
        const val HOMESERVER_KEY = "HOMESERVER_KEY"
    }
}
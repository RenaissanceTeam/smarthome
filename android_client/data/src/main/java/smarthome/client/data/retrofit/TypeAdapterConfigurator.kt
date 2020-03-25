package smarthome.client.data.retrofit

import com.google.gson.GsonBuilder

interface TypeAdapterConfigurator {
    fun configure(builder: GsonBuilder): GsonBuilder
}


